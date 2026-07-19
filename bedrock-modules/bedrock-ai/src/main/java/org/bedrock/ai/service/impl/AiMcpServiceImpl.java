package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.ai.entity.AiMcp;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiMcpMapper;
import org.bedrock.ai.param.AiMcpListParam;
import org.bedrock.ai.param.AiMcpSubmitParam;
import org.bedrock.ai.service.IAiMcpService;
import org.bedrock.ai.vo.AiMcpDetailVO;
import org.bedrock.ai.vo.AiMcpListVO;
import org.bedrock.common.ai.mcp.McpManager;
import org.bedrock.common.ai.mcp.McpManager.McpClientParameters;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.handler.TenantHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiMcpServiceImpl extends BaseServiceImpl<AiMcpMapper, AiMcp> implements IAiMcpService, ApplicationRunner {

    private final McpManager mcpManager;

    @Override
    public boolean submit(AiMcpSubmitParam param) {
        Assert.hasText(param.getName(), "MCP 名称不能为空");
        Assert.notNull(param.getType(), "传输类型不能为空");
        if (exists(Wrappers.<AiMcp>lambdaQuery()
                .eq(AiMcp::getName, param.getName())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(AiErrorEnum.MCP_NAME_ALREADY_EXISTS.getCode(),
                    AiErrorEnum.MCP_NAME_ALREADY_EXISTS.getMessage());
        }
        AiMcp entity = BeanUtil.copyProperties(param, AiMcp.class);
        entity.setId(null);
        entity.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        return save(entity);
    }

    @Override
    public boolean edit(AiMcpSubmitParam param) {
        Assert.notNull(param.getId(), "MCP id 不能为空");
        AiMcp existing = getById(param.getId());
        if (existing == null || BedrockDBConstant.DB_IS_DELETED.equals(existing.getIsDeleted())) {
            throw new ServiceException(AiErrorEnum.MCP_NOT_FOUND.getCode(),
                    AiErrorEnum.MCP_NOT_FOUND.getMessage());
        }
        if (StringUtil.isNotBlank(param.getName()) && !existing.getName().equals(param.getName())) {
            throw new ServiceException(AiErrorEnum.MCP_NAME_IMMUTABLE.getCode(),
                    AiErrorEnum.MCP_NAME_IMMUTABLE.getMessage());
        }
        AiMcp entity = BeanUtil.copyProperties(param, AiMcp.class);
        entity.setName(existing.getName());
        entity.setStatus(existing.getStatus());
        boolean updated = updateById(entity);
        if (updated && BedrockDBConstant.DB_STATUS_NORMAL.equals(existing.getStatus())) {
            registerQuietly(entity);
        }
        return updated;
    }

    @Override
    public boolean removeById(Long id) {
        unregisterQuietly(id);
        return logicRemoveById(id);
    }

    @Override
    public AiMcpDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public List<AiMcpListVO> selectAiMcpList(AiMcpListParam param) {
        return baseMapper.selectAiMcpList(null, param);
    }

    @Override
    public IPage<AiMcpListVO> selectAiMcpListPage(IPage<AiMcpListVO> iPage, AiMcpListParam param) {
        return iPage.setRecords(baseMapper.selectAiMcpList(iPage, param));
    }

    @Override
    public boolean enableStatus(Long id, Integer status) {
        AiMcp existing = getById(id);
        if (existing == null || BedrockDBConstant.DB_IS_DELETED.equals(existing.getIsDeleted())) {
            throw new ServiceException(AiErrorEnum.MCP_NOT_FOUND.getCode(),
                    AiErrorEnum.MCP_NOT_FOUND.getMessage());
        }
        LogRecordContext.putVariable("mcpName", existing.getName());
        LogRecordContext.putVariable("status", status);
        boolean updated = update(Wrappers.<AiMcp>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiMcp::getStatus, status));
        if (!updated) {
            return false;
        }
        if (BedrockDBConstant.DB_STATUS_NORMAL.equals(status)) {
            registerQuietly(existing);
        } else {
            unregisterQuietly(id);
        }
        return true;
    }

    @Override
    public List<McpSchema.Tool> testConnection(AiMcpSubmitParam param) {
        return mcpManager.listTools(toParameters(param));
    }

    @Override
    public void loadEnabledOnStartup() {
        TenantHandler.ignore(() -> {
            List<AiMcp> enabledList = list(Wrappers.<AiMcp>lambdaQuery()
                    .eq(AiMcp::getStatus, BedrockDBConstant.DB_STATUS_NORMAL)
                    .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED));
            for (AiMcp mcp : enabledList) {
                registerQuietly(mcp);
            }
            log.info("Loaded {} enabled MCP client(s) into McpManager", enabledList.size());
        });
    }

    @Override
    public void run(ApplicationArguments args) {
        loadEnabledOnStartup();
    }

    private void registerQuietly(AiMcp mcp) {
        try {
            mcpManager.register(String.valueOf(mcp.getId()), toParameters(mcp));
        } catch (Exception ex) {
            log.error("Failed to register MCP [{}] id={}", mcp.getName(), mcp.getId(), ex);
        }
    }

    private void unregisterQuietly(Long id) {
        try {
            mcpManager.unregister(String.valueOf(id));
        } catch (Exception ex) {
            log.warn("Failed to unregister MCP id={}", id, ex);
        }
    }

    private McpClientParameters toParameters(AiMcp mcp) {
        return toParameters(mcp.getName(), mcp.getType(), mcp.getUrl(), mcp.getEndpoint(),
                mcp.getHeaders(), mcp.getStdioServersJson(), mcp.getClientName(),
                mcp.getRequestTimeoutSeconds() == null ? 0 : mcp.getRequestTimeoutSeconds(),
                mcp.getVersion());
    }

    private McpClientParameters toParameters(AiMcpSubmitParam param) {
        return toParameters(param.getName(), param.getType(), param.getUrl(), param.getEndpoint(),
                param.getHeaders(), param.getStdioServersJson(), param.getClientName(),
                param.getRequestTimeoutSeconds() == null ? 0 : param.getRequestTimeoutSeconds(),
                param.getVersion());
    }

    private McpClientParameters toParameters(String name,
                                             McpClientParameters.TransportType type,
                                             String url,
                                             String endpoint,
                                             Map<String, String> headers,
                                             String stdioServersJson,
                                             String clientName,
                                             int requestTimeoutSeconds,
                                             String version) {
        Assert.notNull(type, "传输类型不能为空");
        return switch (type) {
            case SSE -> McpClientParameters.sse(name, url, endpoint, headers, clientName,
                    requestTimeoutSeconds, version);
            case STREAMABLE_HTTP -> McpClientParameters.streamableHttp(name, url, endpoint, headers,
                    clientName, requestTimeoutSeconds, version);
            case STDIO -> McpClientParameters.stdio(name, stdioServersJson, clientName,
                    requestTimeoutSeconds, version);
        };
    }

}

package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.modelcontextprotocol.spec.McpSchema;
import org.bedrock.ai.entity.AiMcp;
import org.bedrock.ai.param.AiMcpListParam;
import org.bedrock.ai.param.AiMcpSubmitParam;
import org.bedrock.ai.vo.AiMcpDetailVO;
import org.bedrock.ai.vo.AiMcpListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;

import java.util.List;

public interface IAiMcpService extends IBaseService<AiMcp> {

    /**
     * 添加 MCP 配置（默认禁用，不注册到 McpManager）
     */
    boolean submit(AiMcpSubmitParam param);

    /**
     * 修改 MCP 配置（name 不可变；若已启用则重新注册）
     */
    boolean edit(AiMcpSubmitParam param);

    /**
     * 删除 MCP 配置（先注销再逻辑删除）
     */
    boolean removeById(Long id);

    /**
     * 详情
     */
    AiMcpDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiMcpListVO> selectAiMcpList(AiMcpListParam param);

    /**
     * 分页列表
     */
    IPage<AiMcpListVO> selectAiMcpListPage(IPage<AiMcpListVO> iPage, AiMcpListParam param);

    /**
     * 启用 / 禁用（启用注册、禁用注销）
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 连通性测试：临时建连拉取 tools，不写入 McpManager
     */
    List<McpSchema.Tool> testConnection(AiMcpSubmitParam param);

    /**
     * 启动时加载全部已启用 MCP 到 McpManager
     */
    void loadEnabledOnStartup();

}

package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.ai.entity.AiRole;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiRoleMapper;
import org.bedrock.ai.param.AiRoleListParam;
import org.bedrock.ai.param.AiRoleSubmitParam;
import org.bedrock.ai.service.IAiRoleService;
import org.bedrock.ai.vo.AiRoleDetailVO;
import org.bedrock.ai.vo.AiRoleListVO;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.base.TenantEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiRoleServiceImpl extends BaseServiceImpl<AiRoleMapper, AiRole> implements IAiRoleService {

    /**
     * 添加 AI 角色，默认禁用状态
     */
    @Override
    public boolean submit(AiRoleSubmitParam param) {
        if (exists(Wrappers.<AiRole>lambdaQuery()
                .eq(AiRole::getRoleName, param.getRoleName())
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(AiErrorEnum.ROLE_NAME_ALREADY_EXISTS.getCode(), AiErrorEnum.ROLE_NAME_ALREADY_EXISTS.getMessage());
        }
        AiRole aiRole = BeanUtil.copyProperties(param, AiRole.class);
        aiRole.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        return save(aiRole);
    }

    /**
     * 修改 AI 角色
     */
    @Override
    public boolean edit(AiRoleSubmitParam param) {
        if (exists(Wrappers.<AiRole>lambdaQuery()
                .eq(AiRole::getRoleName, param.getRoleName())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(AiErrorEnum.ROLE_NAME_ALREADY_EXISTS.getCode(), AiErrorEnum.ROLE_NAME_ALREADY_EXISTS.getMessage());
        }
        AiRole aiRole = BeanUtil.copyProperties(param, AiRole.class);
        return updateById(aiRole);
    }

    /**
     * 删除 AI 角色
     */
    @Override
    public boolean removeById(Long id) {
        return logicRemoveById(id);
    }

    /**
     * 查询 AI 角色详情
     */
    @Override
    public AiRoleDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    /**
     * 查询 AI 角色无分页列表
     */
    @Override
    public List<AiRoleListVO> selectAiRoleList(AiRoleListParam param) {
        return baseMapper.selectAiRoleList(null, param);
    }

    /**
     * 查询 AI 角色分页列表
     */
    @Override
    public IPage<AiRoleListVO> selectAiRoleListPage(IPage<AiRoleListVO> iPage, AiRoleListParam param) {
        return iPage.setRecords(baseMapper.selectAiRoleList(iPage, param));
    }

    /**
     * 启用禁用 AI 角色
     */
    @Override
    public boolean enableStatus(Long id, Integer status) {
        AiRole aiRole = this.getById(id);
        LogRecordContext.putVariable("roleName", aiRole.getRoleName());
        LogRecordContext.putVariable("status", status);
        return this.update(Wrappers.<AiRole>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiRole::getStatus, status));
    }

}

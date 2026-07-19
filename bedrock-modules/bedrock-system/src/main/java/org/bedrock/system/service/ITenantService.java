package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.Tenant;
import org.bedrock.system.param.TenantEditParam;
import org.bedrock.system.param.TenantEnableParam;
import org.bedrock.system.param.TenantListParam;
import org.bedrock.system.param.TenantSubmitParam;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantListVO;

import java.util.List;

public interface ITenantService extends IBaseService<Tenant> {

    /**
     * 添加
     */
    boolean submit(TenantSubmitParam param);

    /**
     * 修改
     */
    boolean edit(TenantEditParam param);

    /**
     * 分页查询
     */
    IPage<TenantListVO> pageTenant(IPage<TenantListVO> page, TenantListParam param);

    /**
     * 无分页查询
     */
    List<TenantListVO> listTenant(TenantListParam param);

    /**
     * 详情
     */
    TenantDetailVO detail(Long id);

    /**
     * 详情
     */
    TenantDetailVO info(String tenantId);

    /**
     * 启用/禁用
     */
    boolean enableStatus(TenantEnableParam param);

    /**
     * 删除
     */
    boolean deleteById(Long id);
}

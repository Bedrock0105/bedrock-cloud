package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.TenantPackage;
import org.bedrock.system.param.TenantPackageSubmitParam;
import org.bedrock.system.vo.TenantPackageDetailVO;

import java.util.List;

public interface ITenantPackageService extends IBaseService<TenantPackage> {

    /**
     * 添加
     */
    boolean submit(TenantPackageSubmitParam param);

    /**
     * 修改
     */
    boolean edit(TenantPackageSubmitParam param);

    /**
     * 详情
     */
    TenantPackageDetailVO detail(Long id);

    /**
     * 删除
     */
    boolean removeById(Long id);

    /**
     * 分页查询
     */
    IPage<TenantPackage> pageTenantPackage(IPage<TenantPackage> page, TenantPackage param);

    /**
     * 无分页查询
     */
    List<TenantPackage> listTenantPackage(TenantPackage param);
}

package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.TenantPackage;

import java.util.List;

public interface TenantPackageMapper extends BaseMapper<TenantPackage> {

    /**
     * 列表
     */
    List<TenantPackage> listTenantPackage(IPage<TenantPackage> page, @Param("param") TenantPackage param);
}

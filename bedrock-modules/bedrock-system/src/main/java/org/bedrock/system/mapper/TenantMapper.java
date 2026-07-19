package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.Tenant;
import org.bedrock.system.param.TenantListParam;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantListVO;

import java.util.List;

public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 根据id查询租户详情
     */
    TenantDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 根据租户ID查询租户详情
     */
    TenantDetailVO selectDetailByTenantId(@Param("tenantId") String tenantId);

    /**
     * 查询租户列表
     */
    List<TenantListVO> selectListTenantList(IPage<TenantListVO> page, @Param("param") TenantListParam param);
}

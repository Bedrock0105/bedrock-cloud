package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.Admin;
import org.bedrock.system.excel.AdminExportExcel;
import org.bedrock.system.excel.AdminImportExcel;
import org.bedrock.system.param.AdminPageParam;
import org.bedrock.system.vo.AdminDetailVO;
import org.bedrock.system.vo.AdminListVO;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 详情
     */
    AdminDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 分页查询
     */
    List<AdminListVO> selectAdminDetailList(IPage<AdminListVO> page,
                                            @Param("params") AdminPageParam params,
                                            @Param("noRoleAlias") List<String> noRoleAlias);

    List<AdminExportExcel> selectExcelList(@Param("params") AdminPageParam param,
                                           @Param("noRoleAlias") List<String> noRoleAlias);
}

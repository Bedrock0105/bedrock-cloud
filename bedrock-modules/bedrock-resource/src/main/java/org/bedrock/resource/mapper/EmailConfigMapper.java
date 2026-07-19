package org.bedrock.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.resource.entity.EmailConfig;
import org.bedrock.resource.param.EmailConfigListParam;
import org.bedrock.resource.vo.EmailConfigDetailVO;
import org.bedrock.resource.vo.EmailConfigListVO;

import java.util.List;

public interface EmailConfigMapper extends BaseMapper<EmailConfig> {

    /**
     * 根据ID查询详情
     */
    EmailConfigDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 根据编码查询详情
     */
    EmailConfigDetailVO selectDetailByCode(@Param("configCode") String configCode);

    /**
     * 查询列表
     */
    List<EmailConfigListVO> selectEmailConfigList(IPage<EmailConfigListVO> iPage,
                                                  @Param("param") EmailConfigListParam param);
}

package org.bedrock.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.resource.entity.SmsConfig;
import org.bedrock.resource.param.SmsConfigListParam;
import org.bedrock.resource.vo.SmsConfigDetailVO;
import org.bedrock.resource.vo.SmsConfigListVO;

import java.util.List;

public interface SmsConfigMapper extends BaseMapper<SmsConfig> {

    /**
     * 根据ID查询详情
     */
    SmsConfigDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 根据编码查询详情
     */
    SmsConfigDetailVO selectDetailByCode(@Param("configCode") String configCode);

    /**
     * 查询列表
     */
    List<SmsConfigListVO> selectSmsConfigList(IPage<SmsConfigListVO> iPage,
                                              @Param("param") SmsConfigListParam param);
}

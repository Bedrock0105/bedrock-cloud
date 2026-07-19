package org.bedrock.resource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.resource.entity.OssConfig;
import org.bedrock.resource.param.OssConfigListParam;
import org.bedrock.resource.vo.OssConfigDetailVO;
import org.bedrock.resource.vo.OssConfigListVO;

import java.util.List;

public interface OssConfigMapper extends BaseMapper<OssConfig> {

    /**
     * 根据ID查询详情
     */
    OssConfigDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 根据编码查询详情
     */
    OssConfigDetailVO selectDetailByCode(@Param("configCode") String configCode);

    /**
     * 查询列表
     */
    List<OssConfigListVO> selectOssConfigList(IPage<OssConfigListVO> iPage,
                                              @Param("param") OssConfigListParam param);
}

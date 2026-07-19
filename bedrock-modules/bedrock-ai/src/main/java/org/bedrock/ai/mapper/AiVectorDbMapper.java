package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiVectorDb;
import org.bedrock.ai.param.AiVectorDbListParam;
import org.bedrock.ai.vo.AiVectorDbCheckVO;
import org.bedrock.ai.vo.AiVectorDbDetailVO;
import org.bedrock.ai.vo.AiVectorDbListVO;

import java.util.List;

/**
 * 向量数据库配置 Mapper
 */
public interface AiVectorDbMapper extends BaseMapper<AiVectorDb> {

    AiVectorDbDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 查询向量库连接配置及状态，单表查询供运行时校验使用
     */
    AiVectorDbCheckVO selectCheckById(@Param("id") Long id);

    List<AiVectorDbListVO> selectAiVectorDbList(IPage<AiVectorDbListVO> iPage,
            @Param("param") AiVectorDbListParam param);

}

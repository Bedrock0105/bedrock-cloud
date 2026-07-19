package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiModel;
import org.bedrock.ai.param.AiModelListParam;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.ai.vo.AiModelDetailVO;
import org.bedrock.ai.vo.AiModelListVO;

import java.util.List;

public interface AiModelMapper extends BaseMapper<AiModel> {

    /**
     * 根据ID查询详情
     */
    AiModelDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 根据ID查询模型校验信息，含关联 API Key
     */
    AiModelCheckVO selectCheckById(@Param("id") Long id);

    /**
     * 查询列表
     */
    List<AiModelListVO> selectAiModelList(IPage<AiModelListVO> iPage,
                                          @Param("param") AiModelListParam param);

}

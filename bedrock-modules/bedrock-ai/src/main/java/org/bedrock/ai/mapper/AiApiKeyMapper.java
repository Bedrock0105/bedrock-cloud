package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiApiKey;
import org.bedrock.ai.param.AiApiKeyListParam;
import org.bedrock.ai.vo.AiApiKeyDetailVO;
import org.bedrock.ai.vo.AiApiKeyListVO;

import java.util.List;

public interface AiApiKeyMapper extends BaseMapper<AiApiKey> {

    /**
     * 根据ID查询详情
     */
    AiApiKeyDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 查询列表
     */
    List<AiApiKeyListVO> selectAiApiKeyList(IPage<AiApiKeyListVO> iPage,
                                            @Param("param") AiApiKeyListParam param);

}

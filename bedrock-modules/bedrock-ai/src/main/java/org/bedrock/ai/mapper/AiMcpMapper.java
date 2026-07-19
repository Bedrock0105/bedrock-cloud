package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiMcp;
import org.bedrock.ai.param.AiMcpListParam;
import org.bedrock.ai.vo.AiMcpDetailVO;
import org.bedrock.ai.vo.AiMcpListVO;

import java.util.List;

public interface AiMcpMapper extends BaseMapper<AiMcp> {

    /**
     * 根据 ID 查询详情
     */
    AiMcpDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 查询列表
     */
    List<AiMcpListVO> selectAiMcpList(IPage<AiMcpListVO> iPage, @Param("param") AiMcpListParam param);

}

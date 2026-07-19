package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiRole;
import org.bedrock.ai.param.AiRoleListParam;
import org.bedrock.ai.vo.AiRoleDetailVO;
import org.bedrock.ai.vo.AiRoleListVO;

import java.util.List;

public interface AiRoleMapper extends BaseMapper<AiRole> {

    /**
     * 根据ID查询详情
     */
    AiRoleDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 查询列表
     */
    List<AiRoleListVO> selectAiRoleList(IPage<AiRoleListVO> iPage,
                                        @Param("param") AiRoleListParam param);

}

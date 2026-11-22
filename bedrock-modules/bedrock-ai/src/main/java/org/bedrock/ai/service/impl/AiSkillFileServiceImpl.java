package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.entity.AiSkillFile;
import org.bedrock.ai.mapper.AiSkillFileMapper;
import org.bedrock.ai.service.IAiSkillFileService;
import org.bedrock.ai.vo.AiSkillFileVO;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI Skill 文件树服务（骨架，业务未实现）
 */
@Service
@RequiredArgsConstructor
public class AiSkillFileServiceImpl extends BaseServiceImpl<AiSkillFileMapper, AiSkillFile>
        implements IAiSkillFileService {

    @Override
    public List<AiSkillFileVO> listBySkillId(Long skillId) {
        return baseMapper.selectBySkillId(skillId);
    }

    @Override
    public boolean removeBySkillId(Long skillId) {
        return update(Wrappers.<AiSkillFile>lambdaUpdate()
                .eq(AiSkillFile::getSkillId, skillId)
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId())
                .set(AiSkillFile::getIsDeleted, BedrockDBConstant.DB_IS_DELETED));
    }

}

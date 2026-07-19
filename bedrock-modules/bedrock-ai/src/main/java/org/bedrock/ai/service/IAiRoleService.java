package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiRole;
import org.bedrock.ai.param.AiRoleListParam;
import org.bedrock.ai.param.AiRoleSubmitParam;
import org.bedrock.ai.vo.AiRoleDetailVO;
import org.bedrock.ai.vo.AiRoleListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;

import java.util.List;

public interface IAiRoleService extends IBaseService<AiRole> {

    /**
     * 添加 AI 角色
     */
    boolean submit(AiRoleSubmitParam param);

    /**
     * 修改 AI 角色
     */
    boolean edit(AiRoleSubmitParam param);

    /**
     * 删除 AI 角色
     */
    boolean removeById(Long id);

    /**
     * 详情
     */
    AiRoleDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiRoleListVO> selectAiRoleList(AiRoleListParam param);

    /**
     * 分页列表
     */
    IPage<AiRoleListVO> selectAiRoleListPage(IPage<AiRoleListVO> iPage, AiRoleListParam param);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

}

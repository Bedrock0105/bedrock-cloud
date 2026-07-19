package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiApiKey;
import org.bedrock.ai.param.AiApiKeyListParam;
import org.bedrock.ai.param.AiApiKeySubmitParam;
import org.bedrock.ai.vo.AiApiKeyDetailVO;
import org.bedrock.ai.vo.AiApiKeyListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;

import java.util.List;

public interface IAiApiKeyService extends IBaseService<AiApiKey> {

    /**
     * 添加 API Key 配置
     */
    boolean submit(AiApiKeySubmitParam param);

    /**
     * 修改 API Key 配置
     */
    boolean edit(AiApiKeySubmitParam param);

    /**
     * 删除 API Key 配置
     */
    boolean removeById(Long id);

    /**
     * 详情
     */
    AiApiKeyDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiApiKeyListVO> selectAiApiKeyList(AiApiKeyListParam param);

    /**
     * 分页列表
     */
    IPage<AiApiKeyListVO> selectAiApiKeyListPage(IPage<AiApiKeyListVO> iPage, AiApiKeyListParam param);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

}

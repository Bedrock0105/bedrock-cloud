package org.bedrock.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.resource.entity.EmailConfig;
import org.bedrock.resource.param.EmailConfigListParam;
import org.bedrock.resource.param.EmailConfigSubmitParam;
import org.bedrock.resource.vo.EmailConfigDetailVO;
import org.bedrock.resource.vo.EmailConfigListVO;
import org.springframework.lang.Nullable;

import java.util.List;

public interface IEmailConfigService extends IBaseService<EmailConfig> {

    /**
     * 添加
     */
    boolean submit(EmailConfigSubmitParam param);

    /**
     * 修改
     */
    boolean edit(EmailConfigSubmitParam param);

    /**
     * 删除
     *
     * @param id 配置id
     */
    boolean removeById(Long id);

    /**
     * 根据配置编码查询
     *
     * @param configCode 编码，为空时查询启用的配置
     */
    EmailConfigDetailVO getByConfigCode(@Nullable String configCode);

    /**
     * 详情
     */
    EmailConfigDetailVO detail(Long id);

    /**
     * 列表
     */
    List<EmailConfigListVO> selectEmailConfigList(EmailConfigListParam param);

    /**
     * 分页列表
     */
    IPage<EmailConfigListVO> selectEmailConfigListPage(IPage<EmailConfigListVO> iPage, EmailConfigListParam param);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

}

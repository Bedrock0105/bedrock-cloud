package org.bedrock.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.resource.entity.SmsConfig;
import org.bedrock.resource.param.SmsConfigListParam;
import org.bedrock.resource.param.SmsConfigSubmitParam;
import org.bedrock.resource.vo.SmsConfigDetailVO;
import org.bedrock.resource.vo.SmsConfigListVO;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ISmsConfigService extends IBaseService<SmsConfig> {

    /**
     * 添加
     */
    boolean submit(SmsConfigSubmitParam param);

    /**
     * 修改
     */
    boolean edit(SmsConfigSubmitParam param);

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
    SmsConfigDetailVO getByConfigCode(@Nullable String configCode);

    /**
     * 详情
     */
    SmsConfigDetailVO detail(Long id);

    /**
     * 列表
     */
    List<SmsConfigListVO> selectSmsConfigList(SmsConfigListParam param);

    /**
     * 分页列表
     */
    IPage<SmsConfigListVO> selectSmsConfigListPage(IPage<SmsConfigListVO> iPage, SmsConfigListParam param);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

}

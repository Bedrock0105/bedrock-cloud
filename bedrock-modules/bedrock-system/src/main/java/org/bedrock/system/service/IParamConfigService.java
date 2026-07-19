package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.ParamConfig;

public interface IParamConfigService extends IBaseService<ParamConfig> {

    /**
     * 提交
     */
    boolean submit(ParamConfig paramConfig);

    /**
     * 详情
     */
    ParamConfig detail(String configKey);

    /**
     * 分页
     */
    IPage<ParamConfig> page(IPage<ParamConfig> page, ParamConfig paramConfig);

    /**
     * 删除
     */
    boolean removeByCode(String configKey);
}

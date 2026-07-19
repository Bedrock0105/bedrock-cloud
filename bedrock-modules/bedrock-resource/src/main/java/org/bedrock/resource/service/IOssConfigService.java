package org.bedrock.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.resource.entity.OssConfig;
import org.bedrock.resource.param.OssConfigListParam;
import org.bedrock.resource.param.OssConfigSubmitParam;
import org.bedrock.resource.vo.OssConfigDetailVO;
import org.bedrock.resource.vo.OssConfigListVO;
import org.springframework.lang.Nullable;

import java.util.List;

public interface IOssConfigService extends IBaseService<OssConfig> {

    /**
     * 添加
     */
    boolean submit(OssConfigSubmitParam param);

    /**
     * 添加
     */
    boolean edit(OssConfigSubmitParam param);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean removeById(Long id);

    /**
     * 根据配置编码查询
     *
     * @param configCode 编码 是空查询启用的
     */
    OssConfigDetailVO getByConfigCode(@Nullable String configCode);

    /**
     * 详情
     */
    OssConfigDetailVO detail(Long id);

    /**
     * 分页列表
     *
     * @return
     */
    List<OssConfigListVO> selectOssConfigList(OssConfigListParam param);

    /**
     * 分页列表
     */
    IPage<OssConfigListVO> selectOssConfigListPage(IPage<OssConfigListVO> iPage, OssConfigListParam param);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

}

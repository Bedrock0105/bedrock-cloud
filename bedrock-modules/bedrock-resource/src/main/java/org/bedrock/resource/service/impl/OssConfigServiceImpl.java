package org.bedrock.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.resource.entity.OssConfig;
import org.bedrock.resource.enums.ResourceErrorEnum;
import org.bedrock.resource.mapper.OssConfigMapper;
import org.bedrock.resource.param.OssConfigListParam;
import org.bedrock.resource.param.OssConfigSubmitParam;
import org.bedrock.resource.service.IOssConfigService;
import org.bedrock.resource.vo.OssConfigDetailVO;
import org.bedrock.resource.vo.OssConfigListVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OssConfigServiceImpl extends BaseServiceImpl<OssConfigMapper, OssConfig> implements IOssConfigService {

    @Override
    public boolean submit(OssConfigSubmitParam param) {
        if (exists(Wrappers.<OssConfig>lambdaQuery()
                .eq(OssConfig::getConfigCode, param.getConfigCode())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_STATUS_NORMAL))) {
            throw new ServiceException(ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getCode(), ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getMessage());
        }
        OssConfig ossConfig = BeanUtil.copyProperties(param, OssConfig.class);
        return save(ossConfig);
    }

    @Override
    public boolean edit(OssConfigSubmitParam param) {
        if (exists(Wrappers.<OssConfig>lambdaQuery()
                .eq(OssConfig::getConfigCode, param.getConfigCode())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_STATUS_NORMAL)
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getCode(), ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getMessage());
        }
        OssConfig ossConfig = BeanUtil.copyProperties(param, OssConfig.class);
        return updateById(ossConfig);
    }

    @Override
    public boolean removeById(Long id) {
        return logicRemoveById(id);
    }

    @Override
    public OssConfigDetailVO getByConfigCode(String configCode) {
        return baseMapper.selectDetailByCode(configCode);
    }

    @Override
    public OssConfigDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public List<OssConfigListVO> selectOssConfigList(OssConfigListParam param) {
        return baseMapper.selectOssConfigList(null, param);
    }

    @Override
    public IPage<OssConfigListVO> selectOssConfigListPage(IPage<OssConfigListVO> iPage, OssConfigListParam param) {
        return iPage.setRecords(baseMapper.selectOssConfigList(iPage, param));
    }

    @Override
    public boolean enableStatus(Long id, Integer status) {
        /**
         * 启用是 ，把其他的都改成禁用
         */
        if (BedrockDBConstant.DB_STATUS_NORMAL.equals(status)) {
            this.update(Wrappers.<OssConfig>lambdaUpdate()
                    .ne(OssConfig::getId, id)
                    .set(OssConfig::getStatus, BedrockDBConstant.DB_STATUS_DISABLE));
        }
        return this.update(Wrappers.<OssConfig>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(OssConfig::getStatus, status));
    }
}

package org.bedrock.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.resource.entity.SmsConfig;
import org.bedrock.resource.enums.ResourceErrorEnum;
import org.bedrock.resource.mapper.SmsConfigMapper;
import org.bedrock.resource.param.SmsConfigListParam;
import org.bedrock.resource.param.SmsConfigSubmitParam;
import org.bedrock.resource.service.ISmsConfigService;
import org.bedrock.resource.vo.SmsConfigDetailVO;
import org.bedrock.resource.vo.SmsConfigListVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsConfigServiceImpl extends BaseServiceImpl<SmsConfigMapper, SmsConfig> implements ISmsConfigService {

    @Override
    public boolean submit(SmsConfigSubmitParam param) {
        if (exists(Wrappers.<SmsConfig>lambdaQuery()
                .eq(SmsConfig::getConfigCode, param.getConfigCode())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_STATUS_NORMAL))) {
            throw new ServiceException(ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getCode(), ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getMessage());
        }
        SmsConfig smsConfig = BeanUtil.copyProperties(param, SmsConfig.class);
        return save(smsConfig);
    }

    @Override
    public boolean edit(SmsConfigSubmitParam param) {
        if (exists(Wrappers.<SmsConfig>lambdaQuery()
                .eq(SmsConfig::getConfigCode, param.getConfigCode())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_STATUS_NORMAL)
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getCode(), ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getMessage());
        }
        SmsConfig smsConfig = BeanUtil.copyProperties(param, SmsConfig.class);
        return updateById(smsConfig);
    }

    @Override
    public boolean removeById(Long id) {
        return logicRemoveById(id);
    }

    @Override
    public SmsConfigDetailVO getByConfigCode(String configCode) {
        return baseMapper.selectDetailByCode(configCode);
    }

    @Override
    public SmsConfigDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public List<SmsConfigListVO> selectSmsConfigList(SmsConfigListParam param) {
        return baseMapper.selectSmsConfigList(null, param);
    }

    @Override
    public IPage<SmsConfigListVO> selectSmsConfigListPage(IPage<SmsConfigListVO> iPage, SmsConfigListParam param) {
        return iPage.setRecords(baseMapper.selectSmsConfigList(iPage, param));
    }

    @Override
    public boolean enableStatus(Long id, Integer status) {
        /**
         * 启用时，把其他的都改成禁用
         */
        if (BedrockDBConstant.DB_STATUS_NORMAL.equals(status)) {
            this.update(Wrappers.<SmsConfig>lambdaUpdate()
                    .ne(SmsConfig::getId, id)
                    .set(SmsConfig::getStatus, BedrockDBConstant.DB_STATUS_DISABLE));
        }
        return this.update(Wrappers.<SmsConfig>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(SmsConfig::getStatus, status));
    }
}

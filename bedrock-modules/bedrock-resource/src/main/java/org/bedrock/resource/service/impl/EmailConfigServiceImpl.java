package org.bedrock.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.resource.entity.EmailConfig;
import org.bedrock.resource.enums.ResourceErrorEnum;
import org.bedrock.resource.mapper.EmailConfigMapper;
import org.bedrock.resource.param.EmailConfigListParam;
import org.bedrock.resource.param.EmailConfigSubmitParam;
import org.bedrock.resource.service.IEmailConfigService;
import org.bedrock.resource.vo.EmailConfigDetailVO;
import org.bedrock.resource.vo.EmailConfigListVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailConfigServiceImpl extends BaseServiceImpl<EmailConfigMapper, EmailConfig> implements IEmailConfigService {

    @Override
    public boolean submit(EmailConfigSubmitParam param) {
        if (exists(Wrappers.<EmailConfig>lambdaQuery()
                .eq(EmailConfig::getConfigCode, param.getConfigCode())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_STATUS_NORMAL))) {
            throw new ServiceException(ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getCode(), ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getMessage());
        }
        EmailConfig emailConfig = BeanUtil.copyProperties(param, EmailConfig.class);
        return save(emailConfig);
    }

    @Override
    public boolean edit(EmailConfigSubmitParam param) {
        if (exists(Wrappers.<EmailConfig>lambdaQuery()
                .eq(EmailConfig::getConfigCode, param.getConfigCode())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_STATUS_NORMAL)
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getCode(), ResourceErrorEnum.THE_CODE_ALREADY_EXISTS.getMessage());
        }
        EmailConfig emailConfig = BeanUtil.copyProperties(param, EmailConfig.class);
        return updateById(emailConfig);
    }

    @Override
    public boolean removeById(Long id) {
        return logicRemoveById(id);
    }

    @Override
    public EmailConfigDetailVO getByConfigCode(String configCode) {
        return baseMapper.selectDetailByCode(configCode);
    }

    @Override
    public EmailConfigDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public List<EmailConfigListVO> selectEmailConfigList(EmailConfigListParam param) {
        return baseMapper.selectEmailConfigList(null, param);
    }

    @Override
    public IPage<EmailConfigListVO> selectEmailConfigListPage(IPage<EmailConfigListVO> iPage, EmailConfigListParam param) {
        return iPage.setRecords(baseMapper.selectEmailConfigList(iPage, param));
    }

    @Override
    public boolean enableStatus(Long id, Integer status) {
        /**
         * 启用时，把其他的都改成禁用
         */
        if (BedrockDBConstant.DB_STATUS_NORMAL.equals(status)) {
            this.update(Wrappers.<EmailConfig>lambdaUpdate()
                    .ne(EmailConfig::getId, id)
                    .set(EmailConfig::getStatus, BedrockDBConstant.DB_STATUS_DISABLE));
        }
        return this.update(Wrappers.<EmailConfig>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(EmailConfig::getStatus, status));
    }
}

package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.entity.Client;
import org.bedrock.system.mapper.ClientMapper;
import org.bedrock.system.service.IClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl extends BaseServiceImpl<ClientMapper, Client> implements IClientService {

    @Override
    public boolean submit(Client client) {
        return save(client);
    }

    @Override
    public boolean edit(Client client) {
        return updateById(client);
    }

    @Override
    public boolean removeById(Long id) {
        Client client = this.getById(id);
        LogRecordContext.putVariable("clientId", client.getClientId());
        return logicRemoveById(id);
    }

    @Override
    public IPage<Client> pageClient(IPage<Client> page, Client client) {
        return page(page, Wrappers.<Client>lambdaQuery()
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .like(StringUtil.isNotBlank(client.getClientId()), Client::getClientId, client.getClientId()));
    }

    @Override
    public Client detail(Long id) {
        return this.getById(id);
    }
}

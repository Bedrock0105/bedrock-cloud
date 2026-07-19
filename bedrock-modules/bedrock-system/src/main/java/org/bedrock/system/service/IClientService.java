package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.Client;

public interface IClientService extends IBaseService<Client> {

    /**
     * 添加客户端
     */
    boolean submit(Client client);

    /**
     * 修改客户端
     */
    boolean edit(Client client);

    /**
     * 删除客户端
     */
    boolean removeById(Long id);

    /**
     * 查询客户端列表
     */
    IPage<Client> pageClient(IPage<Client> page, Client client);

    /**
     * 查询客户端详情
     */
    Client detail(Long id);
}

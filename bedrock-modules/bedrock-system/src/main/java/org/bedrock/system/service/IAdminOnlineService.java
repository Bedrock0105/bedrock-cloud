package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.system.param.AdminOnlineSubmitParam;
import org.bedrock.system.vo.AdminOnlineListVO;

public interface IAdminOnlineService {

    /**
     * 提交在线用户信息
     *
     * @param param
     */
    void submit(AdminOnlineSubmitParam param);

    /**
     * 心跳
     *
     * @param param
     */
    void heartbeat(AdminOnlineSubmitParam param);

    /**
     * 断开连接
     *
     * @param param
     */
    void close(AdminOnlineSubmitParam param);

    /**
     * 删除 second 秒内没有心跳的用户
     * 或者token过期的用户
     */
    void deleteAdminOnline(int second);

    /**
     * 列表 查询在线的
     */
    IPage<AdminOnlineListVO> list(IPage<AdminOnlineListVO> page);
}

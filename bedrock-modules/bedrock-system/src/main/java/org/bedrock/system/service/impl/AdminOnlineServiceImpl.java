package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.auth.entity.AuthUser;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.entity.AdminOnline;
import org.bedrock.system.mapper.AdminOnlineMapper;
import org.bedrock.system.param.AdminOnlineSubmitParam;
import org.bedrock.system.service.IAdminOnlineService;
import org.bedrock.system.vo.AdminOnlineListVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminOnlineServiceImpl implements IAdminOnlineService {

    private final AdminOnlineMapper adminOnlineMapper;

    @Override
    public void submit(AdminOnlineSubmitParam param) {
        AdminOnline adminOnline = BeanUtil.copyProperties(param, AdminOnline.class);
        String token = AuthUtil.getToken(adminOnline.getToken());
        if (token == null) {
            log.error("保存在线状态TOKEN 为空,{},{},{}", param.getAdminId(), param.getWsOnlyId(), param.getClientIp());
            return;
        }
        AuthUser authUser = AuthUtil.getAuthUser(token);
        adminOnline.setTokenId(authUser.getTokenId());
        adminOnline.setTokenExpired(authUser.getExpireTime());
        adminOnline.setOnlineStatus(BedrockDBConstant.DB_STATUS_NORMAL);
        adminOnline.setLoginTime(LocalDateTime.now());
        adminOnline.setId(IdWorker.getId());
        adminOnline.setLastHeartbeatTime(System.currentTimeMillis());
        adminOnlineMapper.insertForUpdate(adminOnline);
    }

    @Override
    public void heartbeat(AdminOnlineSubmitParam param) {
        int update = adminOnlineMapper.update(Wrappers.<AdminOnline>lambdaUpdate()
                .eq(AdminOnline::getAdminId, param.getAdminId())
                .eq(AdminOnline::getTokenId, param.getTokenId())
                .set(AdminOnline::getOnlineStatus, BedrockDBConstant.DB_STATUS_NORMAL)
                .set(AdminOnline::getLastHeartbeatTime, System.currentTimeMillis()));
        if (update <= 0) {
            submit(param);
        }
    }

    @Override
    public void close(AdminOnlineSubmitParam param) {
        adminOnlineMapper.update(Wrappers.<AdminOnline>lambdaUpdate()
                .eq(AdminOnline::getAdminId, param.getAdminId())
                .eq(AdminOnline::getWsOnlyId, param.getWsOnlyId())
                .set(AdminOnline::getOnlineStatus, BedrockDBConstant.DB_STATUS_DISABLE));
    }

    @Override
    public void deleteAdminOnline(int second) {
        long millis = System.currentTimeMillis();
        millis -= second * 1000L;
        adminOnlineMapper.delete(Wrappers.<AdminOnline>lambdaUpdate()
                .le(AdminOnline::getLastHeartbeatTime, millis)
                .or()
                .le(AdminOnline::getTokenExpired, LocalDateTime.now()));
    }

    @Override
    public IPage<AdminOnlineListVO> list(IPage<AdminOnlineListVO> page) {
        return page.setRecords(adminOnlineMapper.selectAdminOnlineList(page, System.currentTimeMillis() - 120000L));
    }
}

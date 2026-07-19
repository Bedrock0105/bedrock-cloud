package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.AdminOnline;
import org.bedrock.system.vo.AdminOnlineListVO;

import java.util.List;

public interface AdminOnlineMapper extends BaseMapper<AdminOnline> {

    int insertForUpdate(@Param("adminOnline") AdminOnline adminOnline);

    /**
     * 查询在线用户列表
     *
     * @param page 分页参数
     * @return 在线用户列表
     */
    List<AdminOnlineListVO> selectAdminOnlineList(IPage<AdminOnlineListVO> page,
                                                  @Param("lastHeartbeatTime") Long lastHeartbeatTime);
}

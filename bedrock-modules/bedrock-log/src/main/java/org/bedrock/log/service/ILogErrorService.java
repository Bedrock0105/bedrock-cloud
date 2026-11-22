package org.bedrock.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bedrock.common.log.model.LogError;
import org.bedrock.log.param.LogErrorParam;
import org.bedrock.log.vo.LogErrorListVO;

/**
 * 错误日志服务
 */
public interface ILogErrorService extends IService<LogError> {

    /**
     * 分页查询
     */
    IPage<LogErrorListVO> selectPage(IPage<LogErrorListVO> page, LogErrorParam param);

    /**
     * 详情
     */
    LogError detail(Long id);
}

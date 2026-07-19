package org.bedrock.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bedrock.common.log.model.LogOperation;
import org.bedrock.log.param.LogOperationParam;
import org.bedrock.log.vo.LogOperationListVO;

public interface ILogOperationService extends IService<LogOperation> {

    /**
     * 分页查询
     */
    IPage<LogOperationListVO> selectPage(IPage<LogOperationListVO> page, LogOperationParam param);

    /**
     * 详情
     */
    LogOperation detail(Long id);
}

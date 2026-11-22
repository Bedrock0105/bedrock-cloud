package org.bedrock.log.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bedrock.common.log.model.LogError;
import org.bedrock.log.mapper.LogErrorMapper;
import org.bedrock.log.param.LogErrorParam;
import org.bedrock.log.service.ILogErrorService;
import org.bedrock.log.vo.LogErrorListVO;
import org.springframework.stereotype.Service;

/**
 * 错误日志服务实现
 */
@Service
public class LogErrorServiceImpl extends ServiceImpl<LogErrorMapper, LogError> implements ILogErrorService {

    @Override
    public IPage<LogErrorListVO> selectPage(IPage<LogErrorListVO> page, LogErrorParam param) {
        return page.setRecords(baseMapper.selectLogErrorList(page, param));
    }

    @Override
    public LogError detail(Long id) {
        return baseMapper.selectById(id);
    }
}

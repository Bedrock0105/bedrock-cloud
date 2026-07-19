package org.bedrock.log.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bedrock.common.log.model.LogOperation;
import org.bedrock.log.mapper.LogOperationMapper;
import org.bedrock.log.param.LogOperationParam;
import org.bedrock.log.service.ILogOperationService;
import org.bedrock.log.vo.LogOperationListVO;
import org.springframework.stereotype.Service;

@Service
public class LogOperationServiceImpl extends ServiceImpl<LogOperationMapper, LogOperation> implements ILogOperationService {

    @Override
    public IPage<LogOperationListVO> selectPage(IPage<LogOperationListVO> page, LogOperationParam param) {
        return page.setRecords(baseMapper.selectLogOperationList(page, param));
    }

    @Override
    public LogOperation detail(Long id) {
        return baseMapper.selectById(id);
    }
}

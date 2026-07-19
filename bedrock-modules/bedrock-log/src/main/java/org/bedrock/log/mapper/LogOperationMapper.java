package org.bedrock.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.common.log.model.LogOperation;
import org.bedrock.log.param.LogOperationParam;
import org.bedrock.log.vo.LogOperationListVO;

import java.util.List;

public interface LogOperationMapper extends BaseMapper<LogOperation> {

    /**
     * 分页查询
     */
    List<LogOperationListVO> selectLogOperationList(IPage<LogOperationListVO> page,
                                                    @Param("param") LogOperationParam param);

}

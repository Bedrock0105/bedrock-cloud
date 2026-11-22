package org.bedrock.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.common.log.model.LogError;
import org.bedrock.log.param.LogErrorParam;
import org.bedrock.log.vo.LogErrorListVO;

import java.util.List;

/**
 * 错误日志 Mapper
 */
public interface LogErrorMapper extends BaseMapper<LogError> {

    /**
     * 分页查询错误日志
     */
    List<LogErrorListVO> selectLogErrorList(IPage<LogErrorListVO> page,
                                            @Param("param") LogErrorParam param);

}

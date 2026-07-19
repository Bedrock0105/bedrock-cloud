package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.Dict;
import org.bedrock.system.vo.DictDetailVO;
import org.bedrock.system.vo.DictListVO;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {

    DictDetailVO selectDetail(@Param("id") Long id);

    List<DictListVO> selectLazyDictList(IPage<DictListVO> page, @Param("dict") Dict dict);

    List<DictListVO> selectDictValueByDictCode(@Param("dictCode") String dictCode,
                                               @Param("parentId") Long parentId);

}

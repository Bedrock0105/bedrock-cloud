package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.Dict;
import org.bedrock.system.vo.DictDetailVO;
import org.bedrock.system.vo.DictListVO;

import java.util.List;

public interface IDictService extends IBaseService<Dict> {

    /**
     * 提交
     */
    boolean submit(Dict dict);

    /**
     * 修改
     */
    boolean edit(Dict dict);

    /**
     * 删除
     */
    boolean removeById(Long id);

    /**
     * 详情
     */
    DictDetailVO detail(Long id);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 懒加载字典列表
     */
    IPage<DictListVO> lazyDictPage(IPage<DictListVO> page, Dict dict);

    /**
     * 懒加载字典列表
     */
    List<DictListVO> lazyDictList(Dict dict);

    /**
     * 根据字典编码获取字典值
     */
    List<DictListVO> dictValueByDictCode(String dictCode, Long parentId);
}

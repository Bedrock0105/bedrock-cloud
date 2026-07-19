package org.bedrock.system.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.system.vo.DictDetailVO;
import org.bedrock.system.vo.DictListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ApplicationConstant.APPLICATION_SYSTEM_NAME)
public interface IDictClient {

    String SELECT_DICT_DETAIL_BY_CODE = "/feign/dict/detail/code";

    String SELECT_DICT_DETAIL_BY_ID = "/feign/dict/detail/id";

    /**
     * 根据字典编码获取字典值
     *
     * @param dictCode 字典编码
     * @param parentId 上级id
     * @return 字典值
     */
    @GetMapping(SELECT_DICT_DETAIL_BY_CODE)
    R<List<DictListVO>> dictValueByDictCode(@RequestParam("dictCode") String dictCode,
                                            @RequestParam("parentId") Long parentId);

    /**
     * 根据字典id获取字典值
     *
     * @param id 字典id
     * @return 字典值
     */
    @GetMapping(SELECT_DICT_DETAIL_BY_ID)
    R<DictDetailVO> dictValueById(@RequestParam("id") Long id);
}

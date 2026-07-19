package org.bedrock.system.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.system.service.IDictService;
import org.bedrock.system.vo.DictDetailVO;
import org.bedrock.system.vo.DictListVO;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequiredArgsConstructor
public class DictClient implements IDictClient {

    private final IDictService iDictService;

    @Override
    public R<List<DictListVO>> dictValueByDictCode(String dictCode, Long parentId) {
        return R.success(iDictService.dictValueByDictCode(dictCode, parentId));
    }

    @Override
    public R<DictDetailVO> dictValueById(Long id) {
        return R.success(iDictService.detail(id));
    }
}

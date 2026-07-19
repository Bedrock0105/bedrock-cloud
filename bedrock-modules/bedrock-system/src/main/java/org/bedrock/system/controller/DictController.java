package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.util.ObjectUtil;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.system.entity.Dict;
import org.bedrock.system.service.IDictService;
import org.bedrock.system.vo.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "字典 控制器")
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController extends BaseController {

    private final IDictService dictService;

    @PostMapping("/submit")
    @Operation(summary = "添加字典")
    @ApiOperationSupport(order = 1)
    public R<Void> submit(@RequestBody Dict param) {
        return R.status(dictService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改字典")
    @ApiOperationSupport(order = 2)
    public R<Void> edit(@RequestBody Dict param) {
        return R.status(dictService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "字典详情")
    @ApiOperationSupport(order = 3)
    public R<DictDetailVO> detail(Long id) {
        return R.success(dictService.detail(id));
    }

    @PutMapping("/enable-status")
    @Operation(summary = "启用禁用 ", description = "禁用")
    @ApiOperationSupport(order = 4)
    @Parameters({
            @Parameter(name = "id", description = "字典id", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态：0-禁用，1-启用", required = false, in = ParameterIn.QUERY),
    })
    public R<Void> enableStatus(@Parameter(hidden = true) Dict dict) {
        return R.status(dictService.enableStatus(dict.getId(), dict.getStatus()));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除字典")
    @ApiOperationSupport(order = 5)
    public R<Void> remove(Long id) {
        return R.status(dictService.removeById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "字典 懒加载 分页")
    @ApiOperationSupport(order = 6)
    @Parameters({
            @Parameter(name = "dictCode", description = "字典类型", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "dictLabel", description = "字典标签（显示用文本，）", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "parentId", description = "上级id", required = false, in = ParameterIn.QUERY),
    })
    public R<IPage<DictListVO>> page(Query query, @Parameter(hidden = true) Dict dict) {
        return R.success(dictService.lazyDictPage(PageUtil.getPage(query), dict));
    }

    @GetMapping("/list")
    @Operation(summary = "字典 懒加载 无分页")
    @ApiOperationSupport(order = 7)
    @Parameters({
            @Parameter(name = "dictCode", description = "字典类型", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "dictLabel", description = "字典标签（显示用文本，）", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "parentId", description = "上级id", required = false, in = ParameterIn.QUERY),
    })
    public R<List<DictListVO>> list(@Parameter(hidden = true) Dict dict) {
        return R.success(dictService.lazyDictList(dict));
    }

    @GetMapping("/dict-value")
    @Operation(summary = "字典 查询code对应的字典")
    @ApiOperationSupport(order = 8)
    @Parameters({
            @Parameter(name = "dictCode", description = "字典类型", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "parentId", description = "上级id", required = true, in = ParameterIn.QUERY),
    })
    public R<List<DictListVO>> dictValueByDictCode(@Parameter(hidden = true) Dict dict) {
        return R.success(dictService.dictValueByDictCode(dict.getDictCode(), ObjectUtil.isEmpty(dict.getParentId()) ? BedrockDBConstant.DB_TOP_PARENT_ID : dict.getParentId()));
    }

}

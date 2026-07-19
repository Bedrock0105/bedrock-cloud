package org.bedrock.system.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TenantPackageSubmitParam {

    /**
     * 主键
     */
    @Schema(description = "主键")
    private Long id;

    /**
     * 套餐名称
     */
    @Schema(description = "套餐名称")
    private String name;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 菜单id
     */
    @Schema(description = "菜单id")
    private List<String> menuIdList = List.of();
}

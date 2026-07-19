package org.bedrock.system.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class MenuSubmitParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    private String menuName;

    /**
     * 菜单编码（唯一标识）
     */
    @Schema(description = "菜单编码（唯一标识）")
    private String menuCode;

    /**
     * 父菜单ID（0表示根菜单）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "父菜单ID（0表示根菜单）")
    private Long parentId;

    /**
     * 祖级列表（逗号分隔的父ID列表）
     */
    @Schema(description = "祖级列表（逗号分隔的父ID列表）")
    private String ancestors;

    /**
     * 菜单类型：1-目录，2-菜单，3-按钮,4-外部链接
     */
    @Schema(description = "菜单类型：1-目录，2-菜单，3-按钮,4-外部链接")
    private Integer menuType;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String menuIcon;

    /**
     * 排序号（从小到大）
     */
    @Schema(description = "排序号（从小到大）")
    private Integer sort;

    /**
     * 路由路径
     */
    @Schema(description = "路由路径")
    private String routePath;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径")
    private String componentPath;

    /**
     * 外链地址
     */
    @Schema(description = "外链地址")
    private String externalUrl;

    /**
     * 是否缓存页面：0-否，1-是
     */
    @Schema(description = "是否缓存页面：0-否，1-是")
    private Integer isCache;

    /**
     * 是否在标签页中隐藏：0-否，1-是
     */
    @Schema(description = "是否在标签页中隐藏：0-否，1-是")
    private Integer isHideTab;

    /**
     * 是否在菜单中隐藏：0-否，1-是
     */
    @Schema(description = "是否在菜单中隐藏：0-否，1-是")
    private Integer isHide;

    /**
     * 是否固定标签：0-否，1-是（前端多标签页使用）
     */
    @Schema(description = "是否固定标签：0-否，1-是（前端多标签页使用）")
    private Integer isAffix;

    /**
     * 是否内嵌iframe：0-否，1-是
     */
    @Schema(description = "是否内嵌iframe：0-否，1-是")
    private Integer isIframe;

    /**
     * 是否显示徽章：0-否，1-是
     */
    @Schema(description = "是否显示徽章：0-否，1-是")
    private Integer isShowBadge;

    /**
     * 文本徽章
     */
    @Schema(description = "文本徽章")
    private String showTextBadge;

    /**
     * 状态：0-禁用，1-启用
     */
    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    /**
     * 是否全屏显示：0-否，1-是
     */
    @Schema(description = "是否全屏显示：0-否，1-是")
    private Integer isFullScreen;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}

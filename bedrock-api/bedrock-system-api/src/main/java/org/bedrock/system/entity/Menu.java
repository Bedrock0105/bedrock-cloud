package org.bedrock.system.entity;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.system.enums.MenuTypeEnum;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_menu")
public class Menu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

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
     *
     * @see MenuTypeEnum
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
     * 激活路径
     * 用于详情页等隐藏菜单，指定高亮显示的父级菜单路径\n例如：用户详情页高亮显示"用户管理"菜单
     */
    @Schema(description = "激活路径")
    private String activePath;

    /**
     * 是否缓存页面：0-否，1-是
     */
    @Schema(description = "是否缓存页面：0-否，1-是")
    private Integer isCache;

    /**
     * 是否在标签页中隐藏：0-隐藏，1-显示
     */
    @Schema(description = "是否在标签页中隐藏：0-隐藏，1-显示")
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
     * 是否全屏显示：0-否，1-是
     */
    @Schema(description = "是否全屏显示：0-否，1-是")
    private Integer isFullScreen;

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
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}

package org.bedrock.system.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class DictDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 字典类型（分组标识，唯一，如"sys_gender"=性别字典、"sys_order_status"=订单状态字典）
     */
    @Schema(description = "字典类型（分组标识，唯一")
    private String dictCode;

    /**
     * 字典标签（显示用文本，如"男"、"待支付"）
     */
    @Schema(description = "字典标签（显示用文本，")
    private String dictLabel;

    /**
     * 字典值（实际存储/使用的编码，如"1"、"0"）
     */
    @Schema(description = "字典值（实际存储/使用的编码，")
    private String dictValue;

    /**
     * 父字典ID（支持多级字典，顶级字典为0，
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "父字典ID（支持多级字典，顶级字典为0，")
    private Long parentId;

    /**
     * 标签类型（如"primary"、"success"、"warning"、"danger"、"info"）
     */
    @Schema(description = "标签类型（如primary、success、warning、danger、info")
    private String tagType;

    /**
     * 排序号（数值越小越靠前，控制字典项展示顺序）
     */
    @Schema(description = "排序号（数值越小越靠前，控制字典项展示顺序）")
    private Integer sort;

    /**
     * 是否启用：1=启用（可使用），0=禁用（不可使用）
     */
    @Schema(description = "是否启用：1=启用（可使用），0=禁用（不可使用）")
    private Integer status;

    /**
     * 备注（如"该字典项仅用于C端订单展示"）
     */
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "父字典名称")
    private String parentName;

}

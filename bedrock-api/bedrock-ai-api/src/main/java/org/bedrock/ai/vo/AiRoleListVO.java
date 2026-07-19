package org.bedrock.ai.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 角色列表 VO
 */
@Data
public class AiRoleListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色头像 URL
     */
    @Schema(description = "角色头像")
    private String roleAvatar;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    private String remark;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "默认模型 id")
    private Long modelId;

    @Schema(description = "默认模型名称")
    private String modelName;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}

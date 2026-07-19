package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "管理员角色")
@TableName("bedrock_admin_role")
public class AdminRole implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 管理员id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "管理员id")
    private Long adminId;

    /**
     * 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "角色id")
    private Long roleId;
}

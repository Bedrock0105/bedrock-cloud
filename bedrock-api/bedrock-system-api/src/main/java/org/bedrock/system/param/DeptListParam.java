package org.bedrock.system.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeptListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 上级id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "上级id")
    private Long parentId = BedrockDBConstant.DB_TOP_PARENT_ID;

    /**
     * 组织类型
     */
    @Schema(description = "组织类型")
    private Integer category;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;
}

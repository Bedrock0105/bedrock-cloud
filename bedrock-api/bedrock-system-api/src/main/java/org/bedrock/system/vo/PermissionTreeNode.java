package org.bedrock.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bedrock.common.code.tree.INode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionTreeNode implements INode<PermissionTreeNode, Long>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    @Schema(description = "节点ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Setter
    @Getter
    @Schema(description = "父节点ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    @Setter
    @Schema(description = "排序")
    private Integer sort;

    @Getter
    @Setter
    @Schema(description = "类型")
    private Integer type;

    @Getter
    @Setter
    @Schema(description = "名称")
    private String name;

    /**
     * 子孙节点
     */
    @Schema(description = "子节点")
    @Getter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<PermissionTreeNode> children = new ArrayList<>();

    @Override
    public Integer getSort() {
        if (this.sort == null) {
            return 0;
        }
        return this.sort;
    }
}

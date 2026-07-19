package org.bedrock.websocket.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.bedrock.common.code.util.JsonUtil;
import org.bedrock.websocket.enums.ModelEnum;
import org.bedrock.websocket.enums.OperateTypeEnum;

@Data
public class ResultDto {

    /**
     * 标识 操作类型
     */
    private OperateTypeEnum operateType;

    /**
     * 操作模块
     */
    private ModelEnum model;

    /**
     * 核心数据
     */
    private JsonNode data;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}

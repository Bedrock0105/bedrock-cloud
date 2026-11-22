package org.bedrock.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "可清理缓存项")
public record CacheItemVO(@Schema(description = "缓存名称（cacheName）") String cacheName,
                          @Schema(description = "中文说明") String remark) {

}

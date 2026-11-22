package org.bedrock.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bedrock.common.code.monitor.model.JvmMetric;

/**
 * JVM 指标列表/详情行。
 * <p>
 * {@code key} 为汇总 Map 的键，格式一般为 {@code appName@ip:port}（见 HostMetricAggregator#resolveJvmKey）；
 * 页面删除时传此完整 key。{@link JvmMetric#address()} 仅表示网络地址，不是删除键。
 * </p>
 *
 * @param key    JVM 合并键
 * @param metric JVM 指标快照（内存汇总，非持久化）
 */
@Schema(description = "JVM 监控指标行")
public record JvmMetricRowVO(
	@Schema(description = "JVM 合并键，如 appName@ip:port") String key,
	@Schema(description = "JVM 指标快照") JvmMetric metric
) {
}

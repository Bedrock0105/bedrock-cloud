package org.bedrock.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bedrock.common.code.monitor.model.HostMetric;

/**
 * 主机指标列表/详情行。
 * <p>
 * {@code ip} 为汇总 Map 的键，与 {@link HostMetric#ip()} 一致；页面删除时传此 ip。
 * </p>
 *
 * @param ip     主机 IP（汇总键）
 * @param metric 主机指标快照（内存汇总，非持久化）
 */
@Schema(description = "主机监控指标行")
public record HostMetricRowVO(
	@Schema(description = "主机 IP（汇总键）") String ip,
	@Schema(description = "主机指标快照") HostMetric metric
) {
}

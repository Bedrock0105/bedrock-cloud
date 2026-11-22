package org.bedrock.system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.monitor.HostMetricAggregator;
import org.bedrock.common.code.monitor.model.HostMetric;
import org.bedrock.common.code.monitor.model.JvmMetric;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.crypto.annotation.ApiCrypto;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.vo.HostMetricRowVO;
import org.bedrock.system.vo.JvmMetricRowVO;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主机与 JVM 监控查询/清理接口。
 * <p>
 * 数据来自本进程 {@link HostMetricAggregator} 的<strong>内存汇总</strong>（各服务经 EventBus 上报），
 * 非数据库持久化。删除仅从本机 Map 移除展示项，若对端仍在上报则会再次出现。
 * </p>
 * <p>
 * 使用 {@link ObjectProvider} 注入 Aggregator，避免在 {@code @RestController} 上使用
 * {@code @ConditionalOnBean}（组件扫描早于 auto-config，会导致 Controller 永远装不上）。
 * 未开启 monitor / 缺少 OSHI 时接口返回 {@link SystemErrorEnum#MONITOR_NOT_ENABLED}。
 * </p>
 */
@Tag(name = "主机监控 控制器")
@ApiCrypto
@RestController
@RequiredArgsConstructor
@RequestMapping("/host-metric")
public class HostMetricController {

	/**
	 * 可能不存在：monitor 未启用或 auto-config 未装配时为空
	 */
	private final ObjectProvider<HostMetricAggregator> aggregatorProvider;

	/**
	 * 获取 Aggregator；不存在则抛明确业务错误
	 */
	private HostMetricAggregator requireAggregator() {
		HostMetricAggregator aggregator = aggregatorProvider.getIfAvailable();
		if (aggregator == null) {
			throw new ServiceException(SystemErrorEnum.MONITOR_NOT_ENABLED);
		}
		return aggregator;
	}

	/**
	 * 主机列表（按 IP 汇总）
	 */
	@GetMapping("/hosts")
	@Operation(summary = "主机指标列表")
	@ApiOperationSupport(order = 1)
	@PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
	public R<List<HostMetricRowVO>> hosts() {
		Map<String, HostMetric> snapshot = requireAggregator().hostSnapshot();
		List<HostMetricRowVO> list = new ArrayList<>(snapshot.size());
		snapshot.forEach((ip, metric) -> list.add(new HostMetricRowVO(ip, metric)));
		return R.success(list);
	}

	/**
	 * JVM 列表（按 {@code appName@ip:port} 汇总）
	 */
	@GetMapping("/jvms")
	@Operation(summary = "JVM 指标列表")
	@ApiOperationSupport(order = 2)
	@PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
	public R<List<JvmMetricRowVO>> jvms() {
		Map<String, JvmMetric> snapshot = requireAggregator().jvmSnapshot();
		List<JvmMetricRowVO> list = new ArrayList<>(snapshot.size());
		snapshot.forEach((key, metric) -> list.add(new JvmMetricRowVO(key, metric)));
		return R.success(list);
	}

	/**
	 * 主机详情
	 *
	 * @param ip 主机 IP（汇总键）
	 */
	@GetMapping("/host")
	@Operation(summary = "主机指标详情")
	@ApiOperationSupport(order = 3)
	@PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
	public R<HostMetricRowVO> host(@Parameter(description = "主机 IP") @RequestParam String ip) {
		if (StringUtil.isBlank(ip)) {
			throw new ServiceException(SystemErrorEnum.HOST_METRIC_NOT_FOUND);
		}
		HostMetric metric = requireAggregator().hostSnapshot().get(ip.trim());
		if (metric == null) {
			throw new ServiceException(SystemErrorEnum.HOST_METRIC_NOT_FOUND);
		}
		return R.success(new HostMetricRowVO(ip.trim(), metric));
	}

	/**
	 * JVM 详情
	 *
	 * @param key 合并键，如 {@code bedrock-log@192.168.1.8:8081}
	 */
	@GetMapping("/jvm")
	@Operation(summary = "JVM 指标详情")
	@ApiOperationSupport(order = 4)
	@PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
	public R<JvmMetricRowVO> jvm(@Parameter(description = "JVM 合并键 appName@ip:port") @RequestParam String key) {
		if (StringUtil.isBlank(key)) {
			throw new ServiceException(SystemErrorEnum.JVM_METRIC_NOT_FOUND);
		}
		JvmMetric metric = requireAggregator().jvmSnapshot().get(key.trim());
		if (metric == null) {
			throw new ServiceException(SystemErrorEnum.JVM_METRIC_NOT_FOUND);
		}
		return R.success(new JvmMetricRowVO(key.trim(), metric));
	}

	/**
	 * 删除主机汇总条目（不下线真实服务器，仅清本机内存展示）
	 *
	 * @param ip 主机 IP
	 */
	@DeleteMapping("/host")
	@Operation(summary = "删除主机汇总条目")
	@ApiOperationSupport(order = 5)
	@PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
	@OperationLog(type = "主机监控", subType = "删除主机", success = "删除主机汇总：{{#ip}}", condition = "{{#_errorMsg == null}}")
	public R<Void> removeHost(@Parameter(description = "主机 IP") @RequestParam String ip) {
		if (StringUtil.isBlank(ip) || requireAggregator().removeHost(ip.trim()) == null) {
			throw new ServiceException(SystemErrorEnum.HOST_METRIC_NOT_FOUND);
		}
		return R.success();
	}

	/**
	 * 删除 JVM 汇总条目（不停止进程，仅清本机内存展示）
	 *
	 * @param key 合并键 {@code appName@ip:port}
	 */
	@DeleteMapping("/jvm")
	@Operation(summary = "删除 JVM 汇总条目")
	@ApiOperationSupport(order = 6)
	@PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
	@OperationLog(type = "主机监控", subType = "删除JVM", success = "删除JVM汇总：{{#key}}", condition = "{{#_errorMsg == null}}")
	public R<Void> removeJvm(@Parameter(description = "JVM 合并键") @RequestParam String key) {
		if (StringUtil.isBlank(key) || requireAggregator().removeJvm(key.trim()) == null) {
			throw new ServiceException(SystemErrorEnum.JVM_METRIC_NOT_FOUND);
		}
		return R.success();
	}
}

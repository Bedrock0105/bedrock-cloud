package org.bedrock.common.constant;

import org.bedrock.common.code.constant.EnvConstant;
import org.bedrock.common.code.util.StringUtil;

import java.io.File;

public interface SentinelConstant {

    // ====================== 开发环境sentinel配置常量 ======================
    /**
     * dev sentinel服务地址
     */
    String SENTINEL_DEV_SERVER_ADDRESS = "127.0.0.1:7080";
    /**
     * dev sentinel服务日志目录
     */
    String SENTINEL_DEV_SERVER_LOG_DIR = "";
    // ====================== 测试环境sentinel配置常量 ======================
    /**
     * test sentinel服务地址
     */
    String SENTINEL_TEST_SERVER_ADDRESS = "127.0.0.1:7080";
    /**
     * dev sentinel服务日志目录
     */
    String SENTINEL_TEST_SERVER_LOG_DIR = "";
    // ====================== 生产环境sentinel配置常量 ======================
    /**
     * prod sentinel服务地址
     */
    String SENTINEL_PROD_SERVER_ADDRESS = "127.0.0.1:7080";
    /**
     * dev sentinel服务日志目录
     */
    String SENTINEL_PROD_SERVER_LOG_DIR = "";

    /**
     * 根据启动环境获取对应的Sentinel配置信息
     *
     * @param environment 启动环境标识（支持：dev、test、prod）
     * @return SentinelConfig 对应环境的配置信息封装对象
     */
    static SentinelConfig getSentinelConfigByEnvironment(String environment) {
        return switch (environment) {
            case EnvConstant.DEV_CODE -> new SentinelConfig(SENTINEL_DEV_SERVER_ADDRESS, SENTINEL_DEV_SERVER_LOG_DIR);
            case EnvConstant.TEST_CODE ->
                    new SentinelConfig(SENTINEL_TEST_SERVER_ADDRESS, SENTINEL_TEST_SERVER_LOG_DIR);
            case EnvConstant.PROD_CODE ->
                    new SentinelConfig(SENTINEL_PROD_SERVER_ADDRESS, SENTINEL_PROD_SERVER_LOG_DIR);
            default -> throw new IllegalStateException("Unexpected value: " + environment);
        };
    }

    /**
     * Sentinel配置信息封装类（接口内部静态类）
     * 用于统一封装Sentinel连接所需的参数
     */
    record SentinelConfig(String address, String logDir) {

        public SentinelConfig {
            if (StringUtil.isBlank(logDir)) {
                logDir = System.getProperty("user.dir") + File.separator + "logs" + File.separator + "csp" + File.separator;
            }
        }
    }
}

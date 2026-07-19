package org.bedrock.common.constant;

import org.bedrock.common.code.constant.EnvConstant;

public interface NacosConstant {

// ====================== 开发环境Nacos配置常量 ======================
    /**
     * 开发环境Nacos服务器地址（IP:端口）
     */
    String NACOS_DEV_ADDRESS = "127.0.0.1:8848";

    /**
     * 开发环境Nacos访问账号
     */
    String NACOS_DEV_USERNAME = "nacos";

    /**
     * 开发环境Nacos访问密码
     */
    String NACOS_DEV_PASSWORD = "nacos";

    /**
     * 开发环境Nacos命名空间（通常使用环境标识）
     */
    String NACOS_DEV_NAMESPACE = "790c0609-a49f-4fef-b56b-88788f7292bb";

    // ====================== 测试环境Nacos配置常量 ======================
    /**
     * 测试环境Nacos服务器地址（IP:端口）
     */
    String NACOS_TEST_ADDRESS = "127.0.0.1:8848";

    /**
     * 测试环境Nacos访问账号
     */
    String NACOS_TEST_USERNAME = "nacos";

    /**
     * 测试环境Nacos访问密码
     */
    String NACOS_TEST_PASSWORD = "nacos";

    /**
     * 测试环境Nacos命名空间
     */
    String NACOS_TEST_NAMESPACE = "test-namespace";

    // ====================== 生产环境Nacos配置常量 ======================
    /**
     * 生产环境Nacos服务器地址（IP:端口）
     */
    String NACOS_PROD_ADDRESS = "127.0.0.1:8848";

    /**
     * 生产环境Nacos访问账号
     */
    String NACOS_PROD_USERNAME = "nacos";

    /**
     * 生产环境Nacos访问密码
     */
    String NACOS_PROD_PASSWORD = "nacos";

    /**
     * 生产环境Nacos命名空间
     */
    String NACOS_PROD_NAMESPACE = "790c0609-a49f-4fef-b56b-88788f7292bb";

    /**
     * 根据启动环境获取对应的Nacos配置信息
     *
     * @param environment 启动环境标识（支持：dev、test、prod）
     * @return NacosConfig 对应环境的配置信息封装对象
     */
    static NacosConfig getNacosConfigByEnvironment(String environment) {
        return switch (environment) {
            case EnvConstant.DEV_CODE ->
                    new NacosConfig(NACOS_DEV_ADDRESS, NACOS_DEV_USERNAME, NACOS_DEV_PASSWORD, NACOS_DEV_NAMESPACE);
            case EnvConstant.TEST_CODE ->
                    new NacosConfig(NACOS_TEST_ADDRESS, NACOS_TEST_USERNAME, NACOS_TEST_PASSWORD, NACOS_TEST_NAMESPACE);
            case EnvConstant.PROD_CODE ->
                    new NacosConfig(NACOS_PROD_ADDRESS, NACOS_PROD_USERNAME, NACOS_PROD_PASSWORD, NACOS_PROD_NAMESPACE);
            default -> throw new IllegalStateException("Unexpected value: " + environment);
        };
    }

    /**
     * Nacos配置信息封装类（接口内部静态类）
     * 用于统一封装Nacos连接所需的参数
     */
    record NacosConfig(String address, String username, String password, String namespace) {

    }
}

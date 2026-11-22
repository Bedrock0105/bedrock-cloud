package org.bedrock.system.cache;

import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.constant.CacheCommonConstant;
import org.bedrock.system.vo.CacheItemVO;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 可运维清理的缓存白名单。
 * <p>
 * 聚合 {@link CacheConstant} 各 {@code *_CACHE} 与 {@link CacheCommonConstant} 的 dict/oss/param 三个 cacheName，
 * 仅允许清理名单内缓存，避免任意字符串清空。
 * </p>
 */
public final class CacheWhitelist {

    private static final Map<String, String> CACHE_NAME_REMARKS;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(CacheConstant.SYS_CACHE, "系统通用缓存");
        map.put(CacheConstant.USER_CACHE, "用户相关缓存");
        map.put(CacheConstant.ADMIN_CACHE, "管理员相关缓存");
        map.put(CacheConstant.API_CACHE, "API / 接口权限缓存");
        map.put(CacheConstant.SCOPE_CACHE, "数据权限范围缓存");
        map.put(CacheConstant.ROLE_CACHE, "角色相关缓存");
        map.put(CacheConstant.AI_CACHE, "AI 模块缓存");
        map.put(CacheConstant.OAUTH_CACHE, "OAuth 认证授权缓存");
        map.put(CacheCommonConstant.DICT_DETAIL_CACHE_KEY, "字典缓存");
        map.put(CacheCommonConstant.OSS_DETAIL_CACHE_KEY, "OSS 缓存");
        map.put(CacheCommonConstant.PARAM_CONFIG_DETAIL_CACHE_KEY, "参数配置缓存");
        CACHE_NAME_REMARKS = Collections.unmodifiableMap(map);
    }

    private CacheWhitelist() {
    }

    /**
     * 白名单内全部可清理缓存列表。
     */
    public static List<CacheItemVO> list() {
        return CACHE_NAME_REMARKS.entrySet().stream()
                .map(e -> new CacheItemVO(e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * 白名单内全部 cacheName。
     */
    public static List<String> names() {
        return List.copyOf(CACHE_NAME_REMARKS.keySet());
    }

    /**
     * 是否在白名单内。
     */
    public static boolean contains(String cacheName) {
        return cacheName != null && CACHE_NAME_REMARKS.containsKey(cacheName);
    }
}

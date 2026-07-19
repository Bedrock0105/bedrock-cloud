package org.bedrock.common.constant;

import org.bedrock.common.code.constant.CacheConstant;

public interface CacheCommonConstant {

    /**
     * 字典缓存
     */
    String DICT_DETAIL_CACHE_KEY = CacheConstant.PREFIX + "dict:cache";
    /**
     * oss缓存
     */
    String OSS_DETAIL_CACHE_KEY = CacheConstant.PREFIX + "oss:cache";
    /**
     * 参数配置缓存
     */
    String PARAM_CONFIG_DETAIL_CACHE_KEY = CacheConstant.PREFIX + "paramconfig:cache";
}

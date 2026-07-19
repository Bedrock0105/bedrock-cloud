package org.bedrock.system.cache;

import org.bedrock.common.code.util.CacheUtil;
import org.bedrock.common.code.util.SpringUtil;
import org.bedrock.common.constant.CacheCommonConstant;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.enums.DictEnum;
import org.bedrock.system.feign.IDictClient;
import org.bedrock.system.vo.DictDetailVO;
import org.bedrock.system.vo.DictListVO;

import java.util.List;

/**
 * 字典缓存使用类
 */
public final class DictCache {

    private static IDictClient dictClient;

    public static final String DICT_LIST_CODE_PARENTID = "dict:list:code:parentid:";

    public static final String DICT_DETAIL_ID = "dict:detail:id:";

    /**
     * 根据字典编码和字典label获取字典value
     */
    public static String getValue(DictEnum dictEnum, String label) {
        return getValue(dictEnum, label, BedrockDBConstant.DB_TOP_PARENT_ID);
    }

    /**
     * 根据字典编码和字典label获取字典value
     */
    public static String getValue(DictEnum dictEnum, String label, Long parentId) {
        return getValue(dictEnum.getDictCode(), label, parentId);
    }

    /**
     * 根据字典编码和字典label获取字典value
     */
    public static String getValue(String dictCode, String label) {
        return getValue(dictCode, label, BedrockDBConstant.DB_TOP_PARENT_ID);
    }

    /**
     * 根据字典编码和字典label获取字典value
     */
    public static String getValue(String dictCode, String label, Long parentId) {
        return dictByDictCode(dictCode, parentId)
                .stream()
                .filter(dictListVO -> dictListVO.getDictLabel().equals(label))
                .findFirst()
                .map(DictListVO::getDictLabel)
                .orElse(label);

    }

    /**
     * 根据字典编码和字典value获取字典label
     */
    public static String getLabel(DictEnum dictEnum, Object value) {
        return getLabel(dictEnum, value, BedrockDBConstant.DB_TOP_PARENT_ID);
    }

    /**
     * 根据字典编码和字典value获取字典label
     */
    public static String getLabel(DictEnum dictEnum, Object value, Long parentId) {
        return getLabel(dictEnum.getDictCode(), value, parentId);
    }

    /**
     * 根据字典编码和字典value获取字典label
     */
    public static String getLabel(String dictCode, Object value) {
        return getLabel(dictCode, value, BedrockDBConstant.DB_TOP_PARENT_ID);
    }

    /**
     * 根据字典编码和字典value获取字典label
     */
    public static String getLabel(String dictCode, Object value, Long parentId) {
        return dictByDictCode(dictCode, parentId)
                .stream()
                .filter(dictListVO -> dictListVO.getDictValue().equals(value.toString()))
                .findFirst()
                .map(DictListVO::getDictLabel)
                .orElse(value.toString());
    }

    /**
     * 根据字典编码获取字典label列表
     */
    public static String[] dictLabels(DictEnum dictEnum) {
        return dictByDictCode(dictEnum, BedrockDBConstant.DB_TOP_PARENT_ID)
                .stream()
                .map(DictListVO::getDictLabel)
                .toArray(String[]::new);
    }

    /**
     * 根据字典编码获取字典列表
     */
    public static List<DictListVO> dictByDictCode(DictEnum dictEnum, Long parentId) {
        return dictByDictCode(dictEnum.getDictCode(), parentId);
    }

    /**
     * 根据字典编码获取字典列表
     */
    public static List<DictListVO> dictByDictCode(String dictCode, Long parentId) {
        return CacheUtil.get(CacheCommonConstant.DICT_DETAIL_CACHE_KEY,
                DICT_LIST_CODE_PARENTID + dictCode + ":" + parentId,
                () -> getDictClient()
                        .dictValueByDictCode(dictCode, parentId).getData());
    }

    /**
     * 获取字典详情
     */
    public static DictDetailVO dictDetail(Long id) {
        return CacheUtil.get(CacheCommonConstant.DICT_DETAIL_CACHE_KEY,
                DICT_DETAIL_ID + id,
                () -> getDictClient()
                        .dictValueById(id).getData());
    }

    private static IDictClient getDictClient() {
        if (dictClient == null) {
            dictClient = SpringUtil.getBean(IDictClient.class);
        }
        return dictClient;
    }
}

package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.constant.CacheCommonConstant;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.cache.DictCache;
import org.bedrock.system.entity.Dict;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.mapper.DictMapper;
import org.bedrock.system.service.IDictService;
import org.bedrock.system.vo.DictDetailVO;
import org.bedrock.system.vo.DictListVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements IDictService {

    @Override
    @CacheEvict(cacheNames = CacheCommonConstant.DICT_DETAIL_CACHE_KEY, allEntries = true)
    public boolean submit(Dict dict) {
        if (dict.getParentId() == null) {
            dict.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
        }
        if (dict.getParentId() == BedrockDBConstant.DB_TOP_PARENT_ID && exists(Wrappers.<Dict>lambdaQuery()
                .eq(Dict::getDictCode, dict.getDictCode())
                .eq(Dict::getParentId, BedrockDBConstant.DB_TOP_PARENT_ID)
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(SystemErrorEnum.DICT_CODE_EXISTS.getCode(), SystemErrorEnum.DICT_CODE_EXISTS.getMessage());
        } else if (dict.getParentId() != BedrockDBConstant.DB_TOP_PARENT_ID) {
            Dict dictParent = this.getById(dict.getParentId());
            dict.setDictCode(dictParent.getDictCode());
        }
        return save(dict);
    }

    @Override
    @CacheEvict(cacheNames = CacheCommonConstant.DICT_DETAIL_CACHE_KEY, allEntries = true)
    @Transactional
    public boolean edit(Dict dict) {
        if (dict.getParentId() == null) {
            dict.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
        }
        if (dict.getParentId() == BedrockDBConstant.DB_TOP_PARENT_ID && exists(Wrappers.<Dict>lambdaQuery()
                .eq(Dict::getDictCode, dict.getDictCode())
                .eq(Dict::getParentId, BedrockDBConstant.DB_TOP_PARENT_ID)
                .ne(Dict::getId, dict.getId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(SystemErrorEnum.DICT_CODE_EXISTS.getCode(), SystemErrorEnum.DICT_CODE_EXISTS.getMessage());
        }
        Dict detail = this.getById(dict.getId());
        /**
         * 修改所有的字典编码
         */
        update(Wrappers.<Dict>lambdaUpdate()
                .eq(Dict::getDictCode, detail.getDictCode())
                .set(Dict::getDictCode, dict.getDictCode())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED));
        return updateById(dict);
    }

    @Override
    @CacheEvict(cacheNames = CacheCommonConstant.DICT_DETAIL_CACHE_KEY, allEntries = true)
    public boolean removeById(Long id) {
        if (exists(Wrappers.<Dict>lambdaQuery()
                .eq(Dict::getParentId, id))) {
            throw new ServiceException(SystemErrorEnum.DICT_HAS_CHILD.getCode(), SystemErrorEnum.DICT_HAS_CHILD.getMessage());
        }
        return logicRemoveById(id);
    }

    @Override
    public DictDetailVO detail(Long id) {
        return baseMapper.selectDetail(id);
    }

    @Override
    @CacheEvict(cacheNames = CacheCommonConstant.DICT_DETAIL_CACHE_KEY, allEntries = true)
    public boolean enableStatus(Long id, Integer status) {
        return this.update(Wrappers.<Dict>lambdaUpdate()
                .eq(Dict::getId, id)
                .set(Dict::getStatus, status));
    }

    @Override
    public IPage<DictListVO> lazyDictPage(IPage<DictListVO> page, Dict dict) {
        if (dict.getParentId() == null) {
            dict.setParentId(0L);
        }
        return page.setRecords(baseMapper.selectLazyDictList(page, dict));
    }

    @Override
    public List<DictListVO> lazyDictList(Dict dict) {
        if (dict.getParentId() == null) {
            dict.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
        }
        return baseMapper.selectLazyDictList(null, dict);
    }

    @Override
    @Cacheable(cacheNames = CacheCommonConstant.DICT_DETAIL_CACHE_KEY, key = "'" + DictCache.DICT_LIST_CODE_PARENTID + "'+ #dictCode + ':' + #parentId")
    public List<DictListVO> dictValueByDictCode(String dictCode, Long parentId) {
        return baseMapper.selectDictValueByDictCode(dictCode, parentId);
    }
}

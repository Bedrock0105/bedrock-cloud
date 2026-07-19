package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.TreeUtil;
import org.bedrock.common.datascope.support.DefaultScopeModelSupport;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.cache.SystemCache;
import org.bedrock.system.entity.Dept;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.mapper.DeptMapper;
import org.bedrock.system.param.DeptListParam;
import org.bedrock.system.param.DeptSubmitParam;
import org.bedrock.system.service.IDeptService;
import org.bedrock.system.vo.DeptDetailVO;
import org.bedrock.system.vo.DeptListVO;
import org.bedrock.system.vo.DeptTreeVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Override
    @Cacheable(cacheNames = CacheConstant.ADMIN_CACHE, key = "'dept:list:adminid:'+#adminId")
    public List<Dept> selectDeptListByAdminId(Long adminId) {
        return baseMapper.selectDeptListByAdminId(adminId);
    }

    @Override
    @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'dept:tree:all'")
    public boolean submit(DeptSubmitParam param) {
        if (exists(Wrappers.<Dept>lambdaQuery()
                .eq(Dept::getDeptCode, param.getDeptCode()))) {
            throw new ServiceException(SystemErrorEnum.DEPT_CODE_EXISTS);
        }

        Dept dept = BeanUtil.copyProperties(param, Dept.class);
        dept.setId(IdWorker.getId());
        if (dept.getParentId() == null || dept.getParentId() == BedrockDBConstant.DB_TOP_PARENT_ID) {
            dept.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
            dept.setLevel(1);
            dept.setAncestors(dept.getId().toString());
        } else {
            Dept parentDept = baseMapper.selectById(dept.getParentId());
            dept.setLevel(parentDept.getLevel() + 1);
            dept.setAncestors(parentDept.getAncestors() + dept.getId());
        }
        return save(dept);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.DEPT_DETAIL_ID + "' + #param.id"),
            @CacheEvict(cacheNames = CacheConstant.SCOPE_CACHE, key = "'" + DefaultScopeModelSupport.SCOPE_DEPT_CHILD_PREFIX + "' + #param.id"),
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "T(org.bedrock.common.auth.util.AuthUtil).tenantId+'dept:tree:all'"),
    })
    public boolean edit(DeptSubmitParam param) {
        if (exists(Wrappers.<Dept>lambdaQuery()
                .eq(Dept::getDeptCode, param.getDeptCode())
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(SystemErrorEnum.DEPT_CODE_EXISTS);
        }
        Dept dept = BeanUtil.copyProperties(param, Dept.class);
        if (dept.getParentId() == null || dept.getParentId() == BedrockDBConstant.DB_TOP_PARENT_ID) {
            dept.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
            dept.setLevel(1);
            dept.setAncestors(dept.getId().toString());
        } else {
            Dept parentDept = baseMapper.selectById(dept.getParentId());
            dept.setLevel(parentDept.getLevel() + 1);
            dept.setAncestors(parentDept.getAncestors() + dept.getId());
        }
        return updateById(dept);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.DEPT_DETAIL_ID + "' + #id"),
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "T(org.bedrock.common.auth.util.AuthUtil).tenantId+'dept:tree:all'"),
    })
    public boolean removeById(Long id) {
        return this.logicRemoveById(id);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.DEPT_DETAIL_ID + "' + #id")
    public DeptDetailVO detail(Long id) {
        return baseMapper.selectDetailVO(id);
    }

    @Override
    public List<DeptListVO> listDeptByParentId(DeptListParam param) {
        return baseMapper.selectDeptByParentId(param);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_CACHE, key = "T(org.bedrock.common.auth.util.AuthUtil).tenantId+':dept:tree:all:'")
    public List<DeptTreeVO> tree() {
        return TreeUtil.buildTree(baseMapper.selectTreeNodeDept());
    }
}

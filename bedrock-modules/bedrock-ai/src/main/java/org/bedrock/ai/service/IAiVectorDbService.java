package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiVectorDb;
import org.bedrock.ai.param.AiVectorDbListParam;
import org.bedrock.ai.param.AiVectorDbSubmitParam;
import org.bedrock.ai.vo.AiVectorDbCheckVO;
import org.bedrock.ai.vo.AiVectorDbDetailVO;
import org.bedrock.ai.vo.AiVectorDbListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;

import java.util.List;

/**
 * 向量数据库配置服务
 */
public interface IAiVectorDbService extends IBaseService<AiVectorDb> {

    /**
     * 新增向量数据库配置，默认禁用状态
     */
    boolean submit(AiVectorDbSubmitParam param);

    /**
     * 修改向量数据库配置
     */
    boolean edit(AiVectorDbSubmitParam param);

    /**
     * 逻辑删除向量数据库配置
     */
    boolean removeById(Long id);

    /**
     * 查询向量数据库配置详情
     */
    AiVectorDbDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiVectorDbListVO> selectAiVectorDbList(AiVectorDbListParam param);

    /**
     * 分页列表
     */
    IPage<AiVectorDbListVO> selectAiVectorDbListPage(IPage<AiVectorDbListVO> iPage,
                                                     AiVectorDbListParam param);

    /**
     * 启用/禁用向量数据库配置
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 检测向量库是否存在且已启用，并返回连接配置
     * <p>单表查询连接信息，不关联其他业务表；不存在或已禁用则抛出异常</p>
     */
    AiVectorDbCheckVO checkAiVectorDb(Long vectorDbId);

}

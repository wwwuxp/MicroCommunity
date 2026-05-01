package com.hezong.pubcar.dao.impl;


import com.hezong.pubcar.dao.PubcarRepairFormFlowDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.pubcar.PubcarRepairFormFlow;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (PubcarRepairFormFlow)表数据库访问层
 *
 * @author lmf
 * @since 2022-09-21 09:07:30
 */
@Service("pubcarRepairFormFlowDaoImpl")
public class PubcarRepairFormFlowDaoImpl extends BaseServiceDao implements PubcarRepairFormFlowDao {

    @Override
    public PubcarRepairFormFlow queryById(String flowId){
        return sqlSessionTemplate.selectOne("pubcarRepairFormFlowDaoImpl.queryById", flowId);
    }

    @Override
    public List<PubcarRepairFormFlow> queryList(PubcarRepairFormFlow pubcarRepairFormFlow){
        return sqlSessionTemplate.selectList("pubcarRepairFormFlowDaoImpl.queryList", pubcarRepairFormFlow);
    }

    @Override
    public long count(PubcarRepairFormFlow pubcarRepairFormFlow){
        return sqlSessionTemplate.selectOne("pubcarRepairFormFlowDaoImpl.count",pubcarRepairFormFlow);
    }

    @Override
    public int insert(PubcarRepairFormFlow pubcarRepairFormFlow){
        return sqlSessionTemplate.insert("pubcarRepairFormFlowDaoImpl.insert", pubcarRepairFormFlow);
    }

    @Override
    public int insertBatch(List<PubcarRepairFormFlow> entities){
        return sqlSessionTemplate.insert("pubcarRepairFormFlowDaoImpl.insertBatch", entities);
    }

    @Override
    public int insertOrUpdateBatch(List<PubcarRepairFormFlow> entities){
        return sqlSessionTemplate.insert("pubcarRepairFormFlowDaoImpl.insertOrUpdateBatch", entities);
    }
    
    @Override
    public int update(PubcarRepairFormFlow pubcarRepairFormFlow){
        return sqlSessionTemplate.update("pubcarRepairFormFlowDaoImpl.update", pubcarRepairFormFlow);
    }

    @Override
    public int deleteById(String flowId){
        return sqlSessionTemplate.delete("pubcarRepairFormFlowDaoImpl.deleteById", flowId);
    }

    @Override
    public List<PubcarRepairFormFlow> queryListByIds(List<String> ids) {
        return sqlSessionTemplate.selectList("pubcarRepairFormFlowDaoImpl.queryListByIds", ids);
    }
}


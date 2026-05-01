package com.hezong.pubcar.dao.impl;


import com.hezong.pubcar.dao.PubcarRepairFormDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormDto;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormWhereDto;
import com.java110.entity.pubcar.BusinessPubcarRepairForm;
import com.java110.entity.pubcar.PubcarRepairForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (PubcarRepairForm)表数据库访问层
 *
 * @author lmf
 * @since 2022-07-26 17:26:14
 */
@Service("pubcarRepairFormDaoImpl")
public class PubcarRepairFormDaoImpl extends BaseServiceDao implements PubcarRepairFormDao {


    @Override
    public List<PubcarRepairFormDto> quertyPageList(PubcarRepairFormWhereDto entity) {
        return sqlSessionTemplate.selectList("pubcarRepairFormDaoImpl.quertyPageList", entity);
    }

    @Override
    public int quertyPageCount(PubcarRepairFormWhereDto entity) {
        return sqlSessionTemplate.selectOne("pubcarRepairFormDaoImpl.quertyPageCount", entity);
    }

    @Override
    public int saveBusinessPubcarRepairForm(BusinessPubcarRepairForm entity){
        return sqlSessionTemplate.insert("pubcarRepairFormDaoImpl.saveBusinessPubcarRepairForm",entity);
    }

    @Override
    public int savePubcarRepairFormInfo(Map info) {
        return sqlSessionTemplate.insert("pubcarRepairFormDaoImpl.savePubcarRepairFormInfo", info);
    }

    @Override
    public List<BusinessPubcarRepairForm> getBusinessPubcarRepairFormInfo(Map info) {
        return sqlSessionTemplate.selectList("pubcarRepairFormDaoImpl.getBusinessPubcarRepairFormInfo", info);
    }

    @Override
    public PubcarRepairForm queryById(String id){
        return sqlSessionTemplate.selectOne("pubcarRepairFormDaoImpl.queryById", id);
    }

    @Override
    public List<PubcarRepairForm> queryAllByLimit(PubcarRepairForm pubcarRepairForm){
        return sqlSessionTemplate.selectList("pubcarRepairFormDaoImpl.queryAllByLimit", pubcarRepairForm);
    }

    @Override
    public Integer count(PubcarRepairForm pubcarRepairForm){
        return sqlSessionTemplate.selectOne("pubcarRepairFormDaoImpl.count",pubcarRepairForm);
    }

    @Override
    public int insert(PubcarRepairForm pubcarRepairForm){
        return sqlSessionTemplate.insert("pubcarRepairFormDaoImpl.insert", pubcarRepairForm);
    }

    @Override
    public int insertBatch(List<PubcarRepairForm> entities){
        return sqlSessionTemplate.insert("pubcarRepairFormDaoImpl.insertBatch", entities);
    }

    @Override
    public int insertOrUpdateBatch(List<PubcarRepairForm> entities){
        return sqlSessionTemplate.insert("pubcarRepairFormDaoImpl.insertOrUpdateBatch", entities);
    }
    
    @Override
    public int update(PubcarRepairForm pubcarRepairForm){
        return sqlSessionTemplate.update("pubcarRepairFormDaoImpl.update", pubcarRepairForm);
    }

    @Override
    public int deleteById(Integer id){
        return sqlSessionTemplate.delete("pubcarRepairFormDaoImpl.deleteById", id);
    }

}


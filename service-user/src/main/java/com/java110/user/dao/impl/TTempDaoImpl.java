package com.java110.user.dao.impl;


import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.user.TTemp;
import com.java110.user.dao.TTempDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (TTemp)表数据库访问层
 *
 * @author lmf
 * @since 2022-09-20 18:47:01
 */
@Service("tTempDaoImpl")
public class TTempDaoImpl extends BaseServiceDao implements TTempDao {

    @Override
    public TTemp queryById(Long tempId){
        return sqlSessionTemplate.selectOne("tTempDaoImpl.queryById", tempId);
    }

    @Override
    public List<TTemp> queryList(TTemp tTemp){
        return sqlSessionTemplate.selectList("tTempDaoImpl.queryList", tTemp);
    }

    @Override
    public long count(TTemp tTemp){
        return sqlSessionTemplate.selectOne("tTempDaoImpl.count",tTemp);
    }

    @Override
    public int insert(TTemp tTemp){
        return sqlSessionTemplate.insert("tTempDaoImpl.insert", tTemp);
    }

    @Override
    public int insertBatch(List<TTemp> entities){
        return sqlSessionTemplate.insert("tTempDaoImpl.insertBatch", entities);
    }

    @Override
    public int insertOrUpdateBatch(List<TTemp> entities){
        return sqlSessionTemplate.insert("tTempDaoImpl.insertOrUpdateBatch", entities);
    }
    
    @Override
    public int update(TTemp tTemp){
        return sqlSessionTemplate.update("tTempDaoImpl.update", tTemp);
    }

    @Override
    public int deleteById(Long tempId){
        return sqlSessionTemplate.delete("tTempDaoImpl.deleteById", tempId);
    }

    @Override
    public TTemp queryOne(TTemp tTemp) {
        return sqlSessionTemplate.selectOne("tTempDaoImpl.queryOne",tTemp);
    }
}


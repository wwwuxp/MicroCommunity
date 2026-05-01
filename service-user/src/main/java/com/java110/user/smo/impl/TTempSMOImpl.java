package com.java110.user.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.entity.user.TTemp;
import com.java110.intf.user.TTempSMO;
import com.java110.user.dao.TTempDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (TTemp)表接口feign层
 *
 * @author lmf
 * @since 2022-09-20 18:47:02
 */
@RestController
public class TTempSMOImpl extends BaseServiceSMO implements TTempSMO {

    @Autowired
    private TTempDao tTempDao;

    @Override
    public TTemp queryById(@RequestBody Long tempId){
        return tTempDao .queryById(tempId);
    }

    @Override
    public TTemp queryOne(@RequestBody TTemp tTemp){
        return tTempDao .queryOne(tTemp);
    }

    @Override
    public List<TTemp> queryList(@RequestBody TTemp tTemp){
        return tTempDao .queryList(tTemp);
    }

    @Override
    public long count(@RequestBody TTemp tTemp){
        return tTempDao .count(tTemp);
    }

    @Override
    public int insert(@RequestBody TTemp tTemp){
        return tTempDao .insert(tTemp);
    }

    @Override
    public int insertBatch(@RequestBody List<TTemp> entities){
        return tTempDao .insertBatch(entities);
    }

    @Override
    public int insertOrUpdateBatch(@RequestBody List<TTemp> entities){
        return tTempDao .insertOrUpdateBatch(entities);
    }
    
    @Override
    public int update(@RequestBody TTemp tTemp){
        return tTempDao .update(tTemp);
    }

    @Override
    public int deleteById(@RequestBody Long tempId){
        return tTempDao .deleteById(tempId);
    }

}


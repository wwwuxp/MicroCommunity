package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarApprovalProcessServiceDao;
import com.hezong.pubcar.dao.IPubCarApproverServiceDao;
import com.hezong.pubcar.dao.PubcarRepairFormDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormDto;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormWhereDto;
import com.java110.entity.pubcar.PubcarRepairForm;
import com.java110.intf.pubcar.PubcarRepairFormSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author lmf
 * @since 2022-07-26 17:26:17
 */
@RestController
public class PubcarRepairFormSMOImpl extends BaseServiceSMO implements PubcarRepairFormSMO {

    @Autowired
    private PubcarRepairFormDao pubcarRepairFormDao;

    @Autowired
    private IPubCarApprovalProcessServiceDao pubCarApprovalProcessServiceDaoImpl;

    @Autowired
    private IPubCarApproverServiceDao pubCarApproverServiceDaoImpl;


    @Override
    public List<PubcarRepairFormDto> quertyPageList(@RequestBody PubcarRepairFormWhereDto entity) {
        List<PubcarRepairFormDto> pubcarRepairFormDtos = pubcarRepairFormDao.quertyPageList(entity);
        return pubcarRepairFormDtos;
    }

    @Override
    public int quertyPageCount(@RequestBody PubcarRepairFormWhereDto entity) {
        return pubcarRepairFormDao.quertyPageCount(entity);
    }

    @Override
    public PubcarRepairForm queryById(@RequestBody String id){
        return pubcarRepairFormDao .queryById(id);
    }

    @Override
    public List<PubcarRepairForm> queryAllByLimit(PubcarRepairForm pubcarRepairForm){
        return pubcarRepairFormDao .queryAllByLimit(pubcarRepairForm);
    }

    @Override
    public Integer count(@RequestBody PubcarRepairForm pubcarRepairForm){
        return pubcarRepairFormDao .count(pubcarRepairForm);
    }

    @Override
    public int insert(PubcarRepairForm pubcarRepairForm){
        return pubcarRepairFormDao .insert(pubcarRepairForm);
    }

    @Override
    public int insertBatch(List<PubcarRepairForm> entities){
        return pubcarRepairFormDao .insertBatch(entities);
    }

    @Override
    public int insertOrUpdateBatch( List<PubcarRepairForm> entities){
        return pubcarRepairFormDao .insertOrUpdateBatch(entities);
    }
    
    @Override
    public int update(@RequestBody PubcarRepairForm pubcarRepairForm){
        return pubcarRepairFormDao .update(pubcarRepairForm);
    }

    @Override
    public int deleteById(Integer id){
        return pubcarRepairFormDao .deleteById(id);
    }

}


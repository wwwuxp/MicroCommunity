package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarTripCarNumServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import com.java110.intf.pubcar.IPubCarTripCarNumInnerServiceSMO;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 申请车辆数量管理表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarTripCarNumInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarTripCarNumInnerServiceSMO {

    @Autowired
    private IPubCarTripCarNumServiceDao pubCarTripCarNumServiceDaoImpl;


    @Override
    public int savePubCarTripCarNum(@RequestBody PubCarTripCarNumPo pubCarTripCarNumPo) {
        int saveFlag = 1;
        pubCarTripCarNumServiceDaoImpl.savePubCarTripCarNumInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarNumPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarTripCarNum(@RequestBody PubCarTripCarNumPo pubCarTripCarNumPo) {
        int saveFlag = 1;
        pubCarTripCarNumServiceDaoImpl.updatePubCarTripCarNumInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarNumPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarTripCarNum(@RequestBody PubCarTripCarNumPo pubCarTripCarNumPo) {
        int saveFlag = 1;
//        pubCarTripCarNumPo.setStatusCd("1");
        pubCarTripCarNumServiceDaoImpl.updatePubCarTripCarNumInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarNumPo));
        return saveFlag;
    }

    @Override
    public List<PubCarTripCarNumDto> queryPubCarTripCarNums(@RequestBody PubCarTripCarNumDto pubCarTripCarNumDto) {

        //校验是否传了 分页信息

        int page = pubCarTripCarNumDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripCarNumDto.setPage((page - 1) * pubCarTripCarNumDto.getRow());
        }

        return BeanConvertUtil.covertBeanList(pubCarTripCarNumServiceDaoImpl.getPubCarTripCarNumInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarNumDto)), PubCarTripCarNumDto.class);
    }


    @Override
    public int queryPubCarTripCarNumsCount(@RequestBody PubCarTripCarNumDto pubCarTripCarNumDto) {
        return pubCarTripCarNumServiceDaoImpl.queryPubCarTripCarNumsCount(BeanConvertUtil.beanCovertMap(pubCarTripCarNumDto));
    }

    @Override
    public List<PubCarTripCarNumDto> getTripTypeAndNoByTripId(String tripId) {
        return pubCarTripCarNumServiceDaoImpl.getTripTypeAndNoByTripId(tripId);
    }
}

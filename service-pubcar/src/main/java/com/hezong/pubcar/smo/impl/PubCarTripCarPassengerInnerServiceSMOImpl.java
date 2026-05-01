package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarTripCarPassengerServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarTripCarPassenger.PubCarTripCarPassengerDto;
import com.java110.intf.pubcar.IPubCarTripCarPassengerInnerServiceSMO;
import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 乘车人信息管理表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarTripCarPassengerInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarTripCarPassengerInnerServiceSMO {

    @Autowired
    private IPubCarTripCarPassengerServiceDao pubCarTripCarPassengerServiceDaoImpl;


    @Override
    public int savePubCarTripCarPassenger(@RequestBody PubCarTripCarPassengerPo pubCarTripCarPassengerPo) {
        int saveFlag = 1;
        pubCarTripCarPassengerServiceDaoImpl.savePubCarTripCarPassengerInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarPassengerPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarTripCarPassenger(@RequestBody PubCarTripCarPassengerPo pubCarTripCarPassengerPo) {
        int saveFlag = 1;
        pubCarTripCarPassengerServiceDaoImpl.updatePubCarTripCarPassengerInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarPassengerPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarTripCarPassenger(@RequestBody PubCarTripCarPassengerPo pubCarTripCarPassengerPo) {
        int saveFlag = 1;
//        pubCarTripCarPassengerPo.setStatusCd("1");
        pubCarTripCarPassengerServiceDaoImpl.updatePubCarTripCarPassengerInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarPassengerPo));
        return saveFlag;
    }

    @Override
    public List<PubCarTripCarPassengerDto> queryPubCarTripCarPassengers(@RequestBody PubCarTripCarPassengerDto pubCarTripCarPassengerDto) {

        //校验是否传了 分页信息

        int page = pubCarTripCarPassengerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripCarPassengerDto.setPage((page - 1) * pubCarTripCarPassengerDto.getRow());
        }

        return BeanConvertUtil.covertBeanList(pubCarTripCarPassengerServiceDaoImpl.getPubCarTripCarPassengerInfo(BeanConvertUtil.beanCovertMap(pubCarTripCarPassengerDto)), PubCarTripCarPassengerDto.class);
    }


    @Override
    public int queryPubCarTripCarPassengersCount(@RequestBody PubCarTripCarPassengerDto pubCarTripCarPassengerDto) {
        return pubCarTripCarPassengerServiceDaoImpl.queryPubCarTripCarPassengersCount(BeanConvertUtil.beanCovertMap(pubCarTripCarPassengerDto));
    }

}

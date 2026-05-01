package com.hezong.pubcar.bmo.pubCarTripCarPassenger.impl;

import com.hezong.pubcar.bmo.pubCarTripCarPassenger.ISavePubCarTripCarPassengerBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarTripCarPassengerInnerServiceSMO;
import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarTripCarPassengerBMOImpl")
public class SavePubCarTripCarPassengerBMOImpl implements ISavePubCarTripCarPassengerBMO {

    @Autowired
    private IPubCarTripCarPassengerInnerServiceSMO pubCarTripCarPassengerInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarTripCarPassengerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(PubCarTripCarPassengerPo pubCarTripCarPassengerPo) {

        pubCarTripCarPassengerPo.setPassengerId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_CAR_PASSENGER));
        int flag = pubCarTripCarPassengerInnerServiceSMOImpl.savePubCarTripCarPassenger(pubCarTripCarPassengerPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

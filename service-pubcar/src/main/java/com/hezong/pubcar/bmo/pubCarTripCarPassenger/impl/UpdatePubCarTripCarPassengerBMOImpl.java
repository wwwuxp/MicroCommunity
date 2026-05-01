package com.hezong.pubcar.bmo.pubCarTripCarPassenger.impl;

import com.hezong.pubcar.bmo.pubCarTripCarPassenger.IUpdatePubCarTripCarPassengerBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarTripCarPassengerInnerServiceSMO;
import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePubCarTripCarPassengerBMOImpl")
public class UpdatePubCarTripCarPassengerBMOImpl implements IUpdatePubCarTripCarPassengerBMO {

    @Autowired
    private IPubCarTripCarPassengerInnerServiceSMO pubCarTripCarPassengerInnerServiceSMOImpl;

    /**
     * @param pubCarTripCarPassengerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(PubCarTripCarPassengerPo pubCarTripCarPassengerPo) {

        int flag = pubCarTripCarPassengerInnerServiceSMOImpl.updatePubCarTripCarPassenger(pubCarTripCarPassengerPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

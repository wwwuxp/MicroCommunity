package com.hezong.pubcar.bmo.pubCarTrip.impl;

import com.hezong.pubcar.bmo.pubCarTrip.IUpdatePubCarTripBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePubCarTripBMOImpl")
public class UpdatePubCarTripBMOImpl implements IUpdatePubCarTripBMO {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    /**
     * @param pubCarTripPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(PubCarTripPo pubCarTripPo) {

        int flag = pubCarTripInnerServiceSMOImpl.updatePubCarTrip(pubCarTripPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

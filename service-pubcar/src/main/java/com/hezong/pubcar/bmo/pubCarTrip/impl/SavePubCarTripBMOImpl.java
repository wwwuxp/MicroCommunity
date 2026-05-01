package com.hezong.pubcar.bmo.pubCarTrip.impl;

import com.hezong.pubcar.bmo.pubCarTrip.ISavePubCarTripBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarTripBMOImpl")
public class SavePubCarTripBMOImpl implements ISavePubCarTripBMO {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarTripPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarTripPo pubCarTripPo) {

        pubCarTripPo.setTripId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_ID));
        int flag = pubCarTripInnerServiceSMOImpl.savePubCarTrip(pubCarTripPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.pubCarTripMileage.impl;

import com.hezong.pubcar.bmo.pubCarTripMileage.ISavePubCarTripMileageBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarTripMileageBMOImpl")
public class SavePubCarTripMileageBMOImpl implements ISavePubCarTripMileageBMO {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarTripMileagePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarTripMileagePo pubCarTripMileagePo) {

        pubCarTripMileagePo.setMileageId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_MILEAGE));
        int flag = pubCarTripMileageInnerServiceSMOImpl.savePubCarTripMileage(pubCarTripMileagePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

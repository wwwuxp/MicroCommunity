package com.hezong.pubcar.bmo.pubCarTripCarNum.impl;

import com.hezong.pubcar.bmo.pubCarTripCarNum.ISavePubCarTripCarNumBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarTripCarNumInnerServiceSMO;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("savePubCarTripCarNumBMOImpl")
public class SavePubCarTripCarNumBMOImpl implements ISavePubCarTripCarNumBMO {

    @Autowired
    private IPubCarTripCarNumInnerServiceSMO pubCarTripCarNumInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarTripCarNumPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarTripCarNumPo pubCarTripCarNumPo) {

        pubCarTripCarNumPo.setNumId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_CAR_NUM));
        int flag = pubCarTripCarNumInnerServiceSMOImpl.savePubCarTripCarNum(pubCarTripCarNumPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.pubCarTripCarNum.impl;

import com.hezong.pubcar.bmo.pubCarTripCarNum.IUpdatePubCarTripCarNumBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarTripCarNumInnerServiceSMO;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePubCarTripCarNumBMOImpl")
public class UpdatePubCarTripCarNumBMOImpl implements IUpdatePubCarTripCarNumBMO {

    @Autowired
    private IPubCarTripCarNumInnerServiceSMO pubCarTripCarNumInnerServiceSMOImpl;

    /**
     * @param pubCarTripCarNumPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(PubCarTripCarNumPo pubCarTripCarNumPo) {

        int flag = pubCarTripCarNumInnerServiceSMOImpl.updatePubCarTripCarNum(pubCarTripCarNumPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.pubCarTripCarNum.impl;

import com.hezong.pubcar.bmo.pubCarTripCarNum.IDeletePubCarTripCarNumBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarTripCarNumInnerServiceSMO;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deletePubCarTripCarNumBMOImpl")
public class DeletePubCarTripCarNumBMOImpl implements IDeletePubCarTripCarNumBMO {

    @Autowired
    private IPubCarTripCarNumInnerServiceSMO pubCarTripCarNumInnerServiceSMOImpl;

    /**
     * @param pubCarTripCarNumPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(PubCarTripCarNumPo pubCarTripCarNumPo) {

        int flag = pubCarTripCarNumInnerServiceSMOImpl.deletePubCarTripCarNum(pubCarTripCarNumPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

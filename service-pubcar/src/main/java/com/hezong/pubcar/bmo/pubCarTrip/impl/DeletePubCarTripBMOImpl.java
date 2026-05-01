package com.hezong.pubcar.bmo.pubCarTrip.impl;

import com.hezong.pubcar.bmo.pubCarTrip.IDeletePubCarTripBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deletePubCarTripBMOImpl")
public class DeletePubCarTripBMOImpl implements IDeletePubCarTripBMO {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    /**
     * @param pubCarTripPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(PubCarTripPo pubCarTripPo) {

        int flag = pubCarTripInnerServiceSMOImpl.deletePubCarTrip(pubCarTripPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

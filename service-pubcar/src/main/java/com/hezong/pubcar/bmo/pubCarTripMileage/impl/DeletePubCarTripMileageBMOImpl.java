package com.hezong.pubcar.bmo.pubCarTripMileage.impl;

import com.hezong.pubcar.bmo.pubCarTripMileage.IDeletePubCarTripMileageBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deletePubCarTripMileageBMOImpl")
public class DeletePubCarTripMileageBMOImpl implements IDeletePubCarTripMileageBMO {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    /**
     * @param pubCarTripMileagePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(PubCarTripMileagePo pubCarTripMileagePo) {

        int flag = pubCarTripMileageInnerServiceSMOImpl.deletePubCarTripMileage(pubCarTripMileagePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.pubCar.impl;

import com.hezong.pubcar.bmo.pubCar.IUpdatePubCarBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.po.pubCar.PubCarPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updatePubCarBMOImpl")
public class UpdatePubCarBMOImpl implements IUpdatePubCarBMO {

    @Autowired
    private IPubCarInnerServiceSMO pubCarInnerServiceSMOImpl;

    /**
     *
     *
     * @param pubCarPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(PubCarPo pubCarPo) {

        int flag = pubCarInnerServiceSMOImpl.updatePubCar(pubCarPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

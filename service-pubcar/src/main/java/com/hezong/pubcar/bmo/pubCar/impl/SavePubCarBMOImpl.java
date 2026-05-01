package com.hezong.pubcar.bmo.pubCar.impl;

import com.hezong.pubcar.bmo.pubCar.ISavePubCarBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.po.pubCar.PubCarPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("savePubCarBMOImpl")
public class SavePubCarBMOImpl implements ISavePubCarBMO {

    @Autowired
    private IPubCarInnerServiceSMO pubCarInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarPo pubCarPo) {

        pubCarPo.setCarId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_carId));
        int flag = pubCarInnerServiceSMOImpl.savePubCar(pubCarPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

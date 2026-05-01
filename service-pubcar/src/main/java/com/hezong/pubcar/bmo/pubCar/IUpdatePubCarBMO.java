package com.hezong.pubcar.bmo.pubCar;

import com.java110.po.pubCar.PubCarPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePubCarBMO {


    /**
     * 修改车辆管理表
     * add by wuxw
     *
     * @param pubCarPo
     * @return
     */
    ResponseEntity<String> update(PubCarPo pubCarPo);


}

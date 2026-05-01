package com.hezong.pubcar.bmo.pubCar;

import com.java110.po.pubCar.PubCarPo;
import org.springframework.http.ResponseEntity;
public interface ISavePubCarBMO {


    /**
     * 添加车辆管理表
     * add by wuxw
     * @param pubCarPo
     * @return
     */
    ResponseEntity<String> save(PubCarPo pubCarPo);


}

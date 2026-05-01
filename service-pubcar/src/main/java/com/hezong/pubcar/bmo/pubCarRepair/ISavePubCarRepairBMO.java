package com.hezong.pubcar.bmo.pubCarRepair;

import com.java110.po.pubCarRepair.PubCarRepairPo;
import org.springframework.http.ResponseEntity;

public interface ISavePubCarRepairBMO {

    /**
     * 添加车辆维修信息表
     * add by hh
     * @param pubCarRepairPo
     * @return
     */
    ResponseEntity<String> save(PubCarRepairPo pubCarRepairPo);


}

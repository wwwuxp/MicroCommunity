package com.hezong.pubcar.bmo.pubCarRepair;

import com.java110.dto.pubCarRepair.PubCarRepairDto;
import org.springframework.http.ResponseEntity;

public interface IGetPubCarRepairBMO {

    /**
     * 查询车辆维修信息表
     * add by hh
     * @param  pubCarRepairDto
     * @return
     */
    ResponseEntity<String> get(PubCarRepairDto pubCarRepairDto);


}

package com.hezong.pubcar.bmo.pubCarTripMileage;

import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePubCarTripMileageBMO {

    /**
     * 修改行驶里程信息表
     * add by hh
     *
     * @param pubCarTripMileagePo
     * @return
     */
    ResponseEntity<String> update(PubCarTripMileagePo pubCarTripMileagePo);

}

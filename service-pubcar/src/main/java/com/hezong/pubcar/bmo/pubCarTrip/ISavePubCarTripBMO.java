package com.hezong.pubcar.bmo.pubCarTrip;

import com.java110.po.pubCarTrip.PubCarTripPo;
import org.springframework.http.ResponseEntity;
public interface ISavePubCarTripBMO {


    /**
     * 添加行程管理表
     * add by hh
     * @param pubCarTripPo
     * @return
     */
    ResponseEntity<String> save(PubCarTripPo pubCarTripPo);


}

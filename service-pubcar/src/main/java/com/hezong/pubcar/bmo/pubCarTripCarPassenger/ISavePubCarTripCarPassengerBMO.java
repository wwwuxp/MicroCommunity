package com.hezong.pubcar.bmo.pubCarTripCarPassenger;

import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
import org.springframework.http.ResponseEntity;
public interface ISavePubCarTripCarPassengerBMO {


    /**
     * 添加乘车人信息管理表
     * add by hh
     * @param pubCarTripCarPassengerPo
     * @return
     */
    ResponseEntity<String> save(PubCarTripCarPassengerPo pubCarTripCarPassengerPo);


}

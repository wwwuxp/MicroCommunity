package com.hezong.pubcar.bmo.pubCarTripCarNum;

import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import org.springframework.http.ResponseEntity;


public interface IGetPubCarTripCarNumBMO {

    /**
     * 查询申请车辆数量管理表
     * add by hh
     * @param  pubCarTripCarNumDto
     * @return
     */
    ResponseEntity<String> get(PubCarTripCarNumDto pubCarTripCarNumDto);


}

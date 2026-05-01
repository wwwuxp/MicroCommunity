package com.hezong.pubcar.bmo.pubCarTripCarPassenger;
import com.java110.dto.pubCarTripCarPassenger.PubCarTripCarPassengerDto;
import org.springframework.http.ResponseEntity;

public interface IGetPubCarTripCarPassengerBMO {

    /**
     * 查询乘车人信息管理表
     * add by hh
     * @param  pubCarTripCarPassengerDto
     * @return
     */
    ResponseEntity<String> get(PubCarTripCarPassengerDto pubCarTripCarPassengerDto);


}

package com.hezong.pubcar.bmo.pubCarTrip;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import org.springframework.http.ResponseEntity;
public interface IGetPubCarTripBMO {


    /**
     * 查询行程管理表
     * add by hh
     * @param  pubCarTripDto
     * @return
     */
    ResponseEntity<String> get(PubCarTripDto pubCarTripDto);


}

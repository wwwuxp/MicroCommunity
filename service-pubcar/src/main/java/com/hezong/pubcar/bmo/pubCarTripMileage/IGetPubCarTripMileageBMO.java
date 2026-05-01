package com.hezong.pubcar.bmo.pubCarTripMileage;

import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import org.springframework.http.ResponseEntity;

public interface IGetPubCarTripMileageBMO {


    /**
     * 查询行驶里程信息表
     * add by hh
     *
     * @param pubCarTripMileageDto
     * @return
     */
    ResponseEntity<String> get(PubCarTripMileageDto pubCarTripMileageDto);


}

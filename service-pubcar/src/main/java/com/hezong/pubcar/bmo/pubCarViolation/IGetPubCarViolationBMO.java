package com.hezong.pubcar.bmo.pubCarViolation;

import com.java110.dto.pubCarViolation.PubCarViolationDto;
import org.springframework.http.ResponseEntity;

public interface IGetPubCarViolationBMO {


    /**
     * 查询车辆违章信息表
     * add by hh
     *
     * @param pubCarViolationDto
     * @return
     */
    ResponseEntity<String> get(PubCarViolationDto pubCarViolationDto);


}

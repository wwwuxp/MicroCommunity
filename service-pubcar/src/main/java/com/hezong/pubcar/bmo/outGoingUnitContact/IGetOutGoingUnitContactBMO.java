package com.hezong.pubcar.bmo.outGoingUnitContact;

import com.java110.dto.outGoingUnitContact.OutGoingUnitContactDto;
import org.springframework.http.ResponseEntity;

public interface IGetOutGoingUnitContactBMO {

    /**
     * 查询外出单位联系人信息表
     * add by hh
     * @param  outGoingUnitContactDto
     * @return
     */
    ResponseEntity<String> get(OutGoingUnitContactDto outGoingUnitContactDto);


}

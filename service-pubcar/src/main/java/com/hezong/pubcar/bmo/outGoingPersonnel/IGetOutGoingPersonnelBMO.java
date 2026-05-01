package com.hezong.pubcar.bmo.outGoingPersonnel;

import com.java110.dto.outGoingPersonnel.OutGoingPersonnelDto;
import org.springframework.http.ResponseEntity;

public interface IGetOutGoingPersonnelBMO {

    /**
     * 查询外出人员信息表
     * add by hh
     * @param  outGoingPersonnelDto
     * @return
     */
    ResponseEntity<String> get(OutGoingPersonnelDto outGoingPersonnelDto);


}

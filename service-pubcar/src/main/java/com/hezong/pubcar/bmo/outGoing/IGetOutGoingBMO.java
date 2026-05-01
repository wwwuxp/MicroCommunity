package com.hezong.pubcar.bmo.outGoing;

import com.java110.dto.outGoing.OutGoingDto;
import org.springframework.http.ResponseEntity;

public interface IGetOutGoingBMO {

    /**
     * 查询外出信息表
     * add by hh
     * @param  outGoingDto
     * @return
     */
    ResponseEntity<String> get(OutGoingDto outGoingDto);


}

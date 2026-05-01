package com.hezong.pubcar.bmo.pubCarAlarm;

import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import org.springframework.http.ResponseEntity;

public interface IGetPubCarAlarmBMO {

    /**
     * 查询异常报警信息表
     * add by hh
     * @param  pubCarAlarmDto
     * @return
     */
    ResponseEntity<String> get(PubCarAlarmDto pubCarAlarmDto);


}

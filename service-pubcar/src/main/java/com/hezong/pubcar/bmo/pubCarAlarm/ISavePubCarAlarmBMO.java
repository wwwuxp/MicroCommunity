package com.hezong.pubcar.bmo.pubCarAlarm;

import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import org.springframework.http.ResponseEntity;

public interface ISavePubCarAlarmBMO {

    /**
     * 添加异常报警信息表
     * add by hh
     * @param pubCarAlarmPo
     * @return
     */
    ResponseEntity<String> save(PubCarAlarmPo pubCarAlarmPo);


}

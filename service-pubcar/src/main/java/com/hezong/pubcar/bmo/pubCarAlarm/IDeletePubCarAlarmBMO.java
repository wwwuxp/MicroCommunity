package com.hezong.pubcar.bmo.pubCarAlarm;

import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import org.springframework.http.ResponseEntity;

public interface IDeletePubCarAlarmBMO {

    /**
     * 修改异常报警信息表
     * add by hh
     * @param pubCarAlarmPo
     * @return
     */
    ResponseEntity<String> delete(PubCarAlarmPo pubCarAlarmPo);


}

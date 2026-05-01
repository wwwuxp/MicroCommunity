package com.java110.front.components.pubCarAlarm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarAlarm.IAddPubCarAlarmSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加异常报警信息表组件
 * add by hh
 */
@Component("addPubCarAlarm")
public class AddPubCarAlarmComponent {

    @Autowired
    private IAddPubCarAlarmSMO addPubCarAlarmSMOImpl;

    /**
     * 添加异常报警信息表数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addPubCarAlarmSMOImpl.savePubCarAlarm(pd);
    }

}

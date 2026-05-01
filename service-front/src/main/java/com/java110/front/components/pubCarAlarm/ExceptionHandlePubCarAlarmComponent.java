package com.java110.front.components.pubCarAlarm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarAlarm.IEditPubCarAlarmSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 异常处理组件
 * add by hh
 */
@Component("handleExceptionAlarm")
public class ExceptionHandlePubCarAlarmComponent {

    @Autowired
    private IEditPubCarAlarmSMO editPubCarAlarmSMOImpl;

    /**
     * 异常处理数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> handle(IPageData pd){
        return editPubCarAlarmSMOImpl.handleExceptionAlarm(pd);
    }

}

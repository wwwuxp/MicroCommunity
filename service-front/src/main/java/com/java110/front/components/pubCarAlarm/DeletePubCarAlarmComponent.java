package com.java110.front.components.pubCarAlarm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarAlarm.IDeletePubCarAlarmSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加异常报警信息表组件
 */
@Component("deletePubCarAlarm")
public class DeletePubCarAlarmComponent {

    @Autowired
    private IDeletePubCarAlarmSMO deletePubCarAlarmSMOImpl;

    /**
     * 添加异常报警信息表数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePubCarAlarmSMOImpl.deletePubCarAlarm(pd);
    }

}

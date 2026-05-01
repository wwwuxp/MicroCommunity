package com.java110.front.components.pubCarAlarm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarAlarm.IListPubCarAlarmsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询异常报警列表
 * add by hh
 */
@Component("listPubCarAlarm")
public class ListPubCarAlarmComponent {

    @Autowired
    private IListPubCarAlarmsSMO listPubCarAlarmsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarAlarmsSMOImpl.listPubCarAlarms(pd);
    }

}

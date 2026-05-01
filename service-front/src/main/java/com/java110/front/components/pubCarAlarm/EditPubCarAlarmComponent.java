package com.java110.front.components.pubCarAlarm;

import com.java110.front.smo.pubCarAlarm.IEditPubCarAlarmSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 * add by hh
 */
@Component("editPubCarAlarm")
public class EditPubCarAlarmComponent {

    @Autowired
    private IEditPubCarAlarmSMO editPubCarAlarmSMOImpl;

//    public ResponseEntity<String> update(IPageData pd){
//        return editPubCarAlarmSMOImpl.updatePubCarAlarm(pd);
//    }

    public IEditPubCarAlarmSMO getEditPubCarAlarmSMOImpl() {
        return editPubCarAlarmSMOImpl;
    }

    public void setEditPubCarAlarmSMOImpl(IEditPubCarAlarmSMO editPubCarAlarmSMOImpl) {
        this.editPubCarAlarmSMOImpl = editPubCarAlarmSMOImpl;
    }
}

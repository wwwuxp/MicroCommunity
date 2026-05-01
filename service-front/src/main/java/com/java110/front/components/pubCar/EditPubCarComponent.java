package com.java110.front.components.pubCar;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.IConfirmPubCarSMO;
import com.java110.front.smo.pubCar.IEditPubCarSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑车辆组件
 */
@Component("editPubCar")
public class EditPubCarComponent {

    @Autowired
    private IEditPubCarSMO editPubCarSMOImpl;
    @Autowired
    private IConfirmPubCarSMO confirmPubCarSMOImpl;


    /**
     * 修改车辆数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editPubCarSMOImpl.updatePubCar(pd);
    }

    /**
     * 确认已续保、确认已保养、确认已季审或已领标、确认已年审或已领标
     * @param pd
     * @return
     */
    public ResponseEntity<String> confirm(IPageData pd){
        return confirmPubCarSMOImpl.confirmPubCar(pd);
    }

}

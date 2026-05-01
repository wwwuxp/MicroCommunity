package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IEditPubCarTripSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 * add by hh
 */
@Component("editPubCarTrip")
public class EditPubCarTripComponent {

    @Autowired
    private IEditPubCarTripSMO editPubCarTripSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editPubCarTripSMOImpl.updatePubCarTrip(pd);
    }

}

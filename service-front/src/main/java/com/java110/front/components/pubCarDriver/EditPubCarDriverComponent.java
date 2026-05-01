package com.java110.front.components.pubCarDriver;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarDriver.IEditPubCarDriverSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑司机组件
 * add by hh
 */
@Component("editPubCarDriver")
public class EditPubCarDriverComponent {

    @Autowired
    private IEditPubCarDriverSMO editPubCarDriverSMOImpl;

    /**
     * 添加司机数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editPubCarDriverSMOImpl.updatePubCarDriver(pd);
    }

}

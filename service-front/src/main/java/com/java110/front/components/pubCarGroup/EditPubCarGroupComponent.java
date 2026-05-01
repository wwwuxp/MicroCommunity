package com.java110.front.components.pubCarGroup;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarGroup.IEditPubCarGroupSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑分组组件
 * add by hh
 */
@Component("editPubCarGroup")
public class EditPubCarGroupComponent {

    @Autowired
    private IEditPubCarGroupSMO editPubCarGroupSMOImpl;

    /**
     * 添加分组数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editPubCarGroupSMOImpl.updatePubCarGroup(pd);
    }

}

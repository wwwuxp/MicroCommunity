package com.java110.front.components.pubCarUser;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarUser.IEditPubCarUserSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑用车人组件
 * add by hh
 */
@Component("editPubCarUser")
public class EditPubCarUserComponent {

    @Autowired
    private IEditPubCarUserSMO editPubCarUserSMOImpl;

    /**
     * 添加用车人数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editPubCarUserSMOImpl.updatePubCarUser(pd);
    }

}

package com.java110.front.components.pubCarUser;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarUser.IDeletePubCarUserSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除用车人组件
 */
@Component("deletePubCarUser")
public class DeletePubCarUserComponent {

    @Autowired
    private IDeletePubCarUserSMO deletePubCarUserSMOImpl;

    /**
     * 删除用车人管理表数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePubCarUserSMOImpl.deletePubCarUser(pd);
    }
}

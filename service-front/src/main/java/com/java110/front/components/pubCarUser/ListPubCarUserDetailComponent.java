package com.java110.front.components.pubCarUser;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarUser.IListPubCarUsersSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 选择用户列表
 * add by hh
 */
@Component("listPubCarUserDetail")
public class ListPubCarUserDetailComponent {

    @Autowired
    private IListPubCarUsersSMO listPubCarUsersSMOImpl;

    /**
     * 选择用户列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarUsersSMOImpl.listPubCarUserDetails(pd);
    }

}

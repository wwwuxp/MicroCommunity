package com.java110.front.components.pubCarGroup;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarGroup.IListPubCarGroupsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 应用组件管理类
 * add by hh
 */
@Component("listPubCarGroups")
public class ListPubCarGroupComponent {

    @Autowired
    private IListPubCarGroupsSMO listPubCarGroupsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarGroupsSMOImpl.listPubCarGroups(pd);
    }

}

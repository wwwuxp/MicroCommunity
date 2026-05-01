package com.java110.front.components.pubCarGroup;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarGroup.IDeletePubCarGroupSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除分组信息表组件
 */
@Component("deletePubCarGroup")
public class DeletePubCarGroupComponent {

    @Autowired
    private IDeletePubCarGroupSMO deletePubCarGroupSMOImpl;

    /**
     * 删除分组信息表数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePubCarGroupSMOImpl.deletePubCarGroup(pd);
    }

}

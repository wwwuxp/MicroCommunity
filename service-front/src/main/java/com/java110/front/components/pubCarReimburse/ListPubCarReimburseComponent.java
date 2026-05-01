package com.java110.front.components.pubCarReimburse;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarReimburse.IListPubCarReimburseSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询用车报销列表信息
 * @author hh
 * @date 2021/10/12
 */
@Component("listPubCarReimburse")
public class ListPubCarReimburseComponent {

    @Autowired
    private IListPubCarReimburseSMO listPubCarReimburseSMOImpl;

    /**
     * 查询用车报销列表信息
     * @param pd 请求数据封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarReimburseSMOImpl.listPubCarReimburse(pd);
    }

}

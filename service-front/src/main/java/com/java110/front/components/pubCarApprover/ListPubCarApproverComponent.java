package com.java110.front.components.pubCarApprover;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarApprover.IListPubCarApproverSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2021/12/13
 */
@Component("listPubCarApprover")
public class ListPubCarApproverComponent {

    @Autowired
    private IListPubCarApproverSMO listPubCarApproverSMOImpl;

    /**
     * 查询公车审批人信息
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listPubCarApproverSMOImpl.listPubCarApprover(pd);
    }
}

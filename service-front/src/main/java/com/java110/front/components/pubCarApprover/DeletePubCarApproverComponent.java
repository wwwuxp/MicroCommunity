package com.java110.front.components.pubCarApprover;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarApprover.IDeletePubCarApproverSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除用车审批人信息
 * @author hh
 * @date 2021/12/13
 */
@Component("deletePubCarApprover")
public class DeletePubCarApproverComponent {

    @Autowired
    private IDeletePubCarApproverSMO deletePubCarApproverSMOImpl;

    /**
     * 删除公车审批人信息
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> delete(IPageData pd){
        return deletePubCarApproverSMOImpl.deletePubCarApprover(pd);
    }

}

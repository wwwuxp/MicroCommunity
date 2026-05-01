package com.java110.front.components.pubCarReimburse;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarReimburse.IAuditPubCarReimburseSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2021/10/12
 */
@Component("auditPubCarReimburse")
public class AuditPubCarReimburseComponent {

    @Autowired
    private IAuditPubCarReimburseSMO auditPubCarReimburseSMOImpl;

    /**
     * 审批用车报销信息
     * @param pd 请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> audit(IPageData pd){
        return auditPubCarReimburseSMOImpl.auditPubCarReimburse(pd);
    }

}

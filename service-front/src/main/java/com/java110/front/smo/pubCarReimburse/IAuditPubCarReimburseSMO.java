package com.java110.front.smo.pubCarReimburse;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/10/12
 */
public interface IAuditPubCarReimburseSMO {

    /**
     * 审核用车报销信息
     * @param pd 请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> auditPubCarReimburse(IPageData pd);

}

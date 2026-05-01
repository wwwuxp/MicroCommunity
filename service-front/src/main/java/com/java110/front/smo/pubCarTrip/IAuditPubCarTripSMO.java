package com.java110.front.smo.pubCarTrip;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 审核行程接口
 * add by hh
 */
public interface IAuditPubCarTripSMO {

    ResponseEntity<String> auditPubCarTrip(IPageData pd);

}

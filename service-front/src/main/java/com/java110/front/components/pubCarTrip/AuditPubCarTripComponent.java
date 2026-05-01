package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IAuditPubCarTripSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 审核行程 组件
 * add by hh
 */
@Component("auditPubCarTrip")
public class AuditPubCarTripComponent {

    @Autowired
    private IAuditPubCarTripSMO auditPubCarTripSMOImpl;

    public ResponseEntity<String> audit(IPageData pd){
        return auditPubCarTripSMOImpl.auditPubCarTrip(pd);
    }

}

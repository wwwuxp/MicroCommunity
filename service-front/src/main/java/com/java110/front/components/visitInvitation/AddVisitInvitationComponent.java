package com.java110.front.components.visitInvitation;

import com.java110.core.context.IPageData;
import com.java110.front.smo.visitInvitation.IAddVisitInvitationSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * create date 2021/9/23
 */
@Component("addVisitInvitation")
public class AddVisitInvitationComponent {

    @Autowired
    private IAddVisitInvitationSMO addVisitInvitationSMOImpl;

    public ResponseEntity<String> save(IPageData pd){
        return addVisitInvitationSMOImpl.addVisitInvitation(pd);
    }
}

package com.java110.front.components.visitInvitation;

import com.java110.core.context.IPageData;
import com.java110.front.smo.visitInvitation.IEditVisitInvitationSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * create date 2021/9/23
 */
@Component("editVisitInvitation")
public class EditVisitInvitationComponent {

    @Autowired
    private IEditVisitInvitationSMO editVisitInvitationSMOImpl;

    public ResponseEntity<String> update(IPageData pd){
        return editVisitInvitationSMOImpl.editVisitInvitation(pd);
    }
}

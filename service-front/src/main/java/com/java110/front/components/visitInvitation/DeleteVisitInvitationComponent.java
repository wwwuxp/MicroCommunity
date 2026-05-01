package com.java110.front.components.visitInvitation;

import com.java110.core.context.IPageData;
import com.java110.front.smo.visitInvitation.IDeleteVisitInvitationSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * create date 2021/9/23
 */
@Component("deleteVisitInvitation")
public class DeleteVisitInvitationComponent {

    @Autowired
    private IDeleteVisitInvitationSMO deleteVisitInvitationSMOImpl;

    public ResponseEntity<String> delete(IPageData pd){
        return deleteVisitInvitationSMOImpl.deleteVisitInvitation(pd);
    }

}

package com.java110.front.components.visitInvitation;

import com.java110.core.context.IPageData;
import com.java110.front.smo.visitInvitation.IListVisitInvitationSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * pc端访客邀约信息列表查询
 * @author hh
 * create date 2021/9/22
 */
@Component("listVisitInvitations")
public class ListVisitInvitationComponent {

    @Autowired
    private IListVisitInvitationSMO listVisitInvitationSMOImpl;

    public ResponseEntity<String> list(IPageData pd){
        return listVisitInvitationSMOImpl.listVisitInvitations(pd);
    }

}

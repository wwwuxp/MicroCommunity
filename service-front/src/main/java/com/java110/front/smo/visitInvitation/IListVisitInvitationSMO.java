package com.java110.front.smo.visitInvitation;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * create date 2021/9/22
 */
public interface IListVisitInvitationSMO {

    ResponseEntity<String> listVisitInvitations(IPageData pd);

}

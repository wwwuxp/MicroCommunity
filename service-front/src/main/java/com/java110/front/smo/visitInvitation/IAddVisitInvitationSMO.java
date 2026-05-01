package com.java110.front.smo.visitInvitation;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * create date 2021/9/23
 */
public interface IAddVisitInvitationSMO {

    ResponseEntity<String> addVisitInvitation(IPageData pd);

}

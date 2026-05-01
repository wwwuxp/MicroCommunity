package com.java110.front.smo.pubCar;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 确认已续保、确认已保养、确认已季审或已领标、确认已年审或已领标
 * @author hh
 * @datetime 2021/8/25 9:09
 */
public interface IConfirmPubCarSMO {

    ResponseEntity<String> confirmPubCar(IPageData pd);

}

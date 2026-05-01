package com.java110.front.smo.pubCar;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改车辆管理表接口
 *
 * add by hh
 */
public interface IEditPubCarSMO {

    ResponseEntity<String> updatePubCar(IPageData pd);

}

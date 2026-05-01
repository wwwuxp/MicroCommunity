package com.java110.front.smo.pubCarTrip;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改行程管理表接口
 * add by hh
 */
public interface IEditPubCarTripSMO {

    /**
     * 修改小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updatePubCarTrip(IPageData pd);
}

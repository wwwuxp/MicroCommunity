package com.java110.front.smo.pubCarTrip;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加行程管理表接口
 * add by hh
 */
public interface IAddPubCarTripSMO {

    /**
     * 添加行程管理表
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> savePubCarTrip(IPageData pd);
}

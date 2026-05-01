package com.java110.front.smo.pubCarDriver;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改司机信息表接口
 * add by hh
 */
public interface IEditPubCarDriverSMO {

    /**
     * 修改小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updatePubCarDriver(IPageData pd);
}

package com.java110.front.smo.pubCarDriver;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加司机信息表接口
 * add by hh
 */
public interface IAddPubCarDriverSMO {

    /**
     * 添加司机信息表
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> savePubCarDriver(IPageData pd);
}

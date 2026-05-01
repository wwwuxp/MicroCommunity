package com.java110.front.smo.pubCarGroup;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加分组信息表接口
 * add by hh
 */
public interface IAddPubCarGroupSMO {

    /**
     * 添加分组信息表
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> savePubCarGroup(IPageData pd);
}

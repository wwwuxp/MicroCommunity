package com.java110.front.smo.pubCarUser;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加用车人管理表接口
 * add by hh
 */
public interface IAddPubCarUserSMO {

    /**
     * 添加用车人管理表
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> savePubCarUser(IPageData pd);
}

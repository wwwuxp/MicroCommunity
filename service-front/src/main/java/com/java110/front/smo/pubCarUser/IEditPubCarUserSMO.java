package com.java110.front.smo.pubCarUser;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改用车人管理表接口
 * add by hh
 */
public interface IEditPubCarUserSMO {

    /**
     * 修改用车人
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updatePubCarUser(IPageData pd);
}

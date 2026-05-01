package com.java110.front.smo.keyCabinetSn;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/2/18
 */
public interface IAddKeyCabinetSnSMO {

    /**
     * 保存钥匙柜信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<String> addKeyCabinetSn(IPageData pd);

}

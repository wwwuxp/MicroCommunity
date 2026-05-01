package com.java110.front.smo.keyCabinetLog;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/2/14
 */
public interface IListKeyCabinetLogSMO {

    /**
     * 查询取还钥匙日志记录
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<String> listKeyCabinetLog(IPageData pd);

}

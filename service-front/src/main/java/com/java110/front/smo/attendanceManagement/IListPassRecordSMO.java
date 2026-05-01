package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/1/26
 */
public interface IListPassRecordSMO {

    /**
     * 查询通行记录数据列表
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<String> listPassRecord(IPageData pd);

}

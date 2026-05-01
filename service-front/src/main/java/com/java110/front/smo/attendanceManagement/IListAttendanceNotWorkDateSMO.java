package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author lmf
 * @date 2022/6/29
 */
public interface IListAttendanceNotWorkDateSMO {

    /**
     * 非工作日列表查询
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> listAttendanceNotWorkDate(IPageData pd);
}

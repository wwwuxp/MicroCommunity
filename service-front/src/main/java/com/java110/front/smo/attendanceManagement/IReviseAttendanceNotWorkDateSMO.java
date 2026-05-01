package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author lmf
 * 非工作日修改的SMO
 * @date 2022/6/30
 */
public interface IReviseAttendanceNotWorkDateSMO {

    /**
     * 非工作日新增
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> saveOrEdit(IPageData pd);

    /**
     * 非工作日删除
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> delete(IPageData pd);



}

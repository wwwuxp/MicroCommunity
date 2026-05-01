package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IReviseAttendanceNotWorkDateSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author lmf
 * @date 2022/6/30
 */
@Component("reviseAttendanceNotWorkDate")
public class ReviseAttendanceNotWorkDateComponent {

    @Autowired
    private IReviseAttendanceNotWorkDateSMO reviseAttendanceNotWorkDateSMO;

    /**
     * 删除非工作日数据
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> delete(IPageData pd){
        return reviseAttendanceNotWorkDateSMO.delete(pd);
    }

    /**
     * 新增/修改非工作日
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> saveOrEdit(IPageData pd){
        return reviseAttendanceNotWorkDateSMO.saveOrEdit(pd);
    }

}

package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListAttendanceFileSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询到导入的文件列表
 * @author hh
 * @date 2021/9/27
 */
@Component("listAttendanceFile")
public class ListAttendanceFileComponent {

    @Autowired
    private IListAttendanceFileSMO listAttendanceFileSMOImpl;

    public ResponseEntity<String> list(IPageData pd) {
        return listAttendanceFileSMOImpl.listAttendanceFile(pd);
    }

}

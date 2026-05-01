package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IDeleteAttendanceFileSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除导入的文件
 * @author hh
 * @date 2021/9/27
 */
@Component("deleteAttendanceFile")
public class DeleteAttendanceFileComponent {

    @Autowired
    private IDeleteAttendanceFileSMO deleteAttendanceFileSMOImpl;

    public ResponseEntity<String> delete(IPageData pd) {
        return deleteAttendanceFileSMOImpl.deleteAttendanceFile(pd);
    }
}

package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/9/27
 */
public interface IListAttendanceFileSMO {

    /**
     * 查询导入的文件列表
     * @param pd 封装数据
     * @return ResponseEntity
     */
    ResponseEntity<String> listAttendanceFile(IPageData pd);

}

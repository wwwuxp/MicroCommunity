package com.java110.front.smo.attendanceManagement;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hh
 * create date 2021/9/24
 */
public interface IImportAttendanceFileSMO {

    /**
     * 导入Excel数据
     * @param pd 数据封装
     * @param uploadFile 上传文件
     * @return ResponseEntity
     * @throws Exception Exception
     */
    ResponseEntity<String> importExcelData(IPageData pd, MultipartFile uploadFile) throws Exception;

}

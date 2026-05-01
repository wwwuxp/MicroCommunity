package com.java110.vo.api.attendanceFile;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/9/27
 */
public class ApiAttendanceFileDataVo implements Serializable {

    private static final long serialVersionUID = -6294976490930948716L;

    private Integer id;
    private String fileName;
    private String fileTime;
    private Integer dataType;
    private String importTime;
    private String uploader;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }
}

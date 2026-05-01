package com.java110.dto.attendance;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hh
 * @date 2021/9/27
 */
public class AttendanceFileDto extends PageDto implements Serializable {

    private static final long serialVersionUID = -5265463519484905320L;

    private String fileName;
    private Date fileTime;
    private Integer dataType;
    private String uploader;

    private String startTime;
    private String endTime;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getFileTime() {
        return fileTime;
    }

    public void setFileTime(Date fileTime) {
        this.fileTime = fileTime;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

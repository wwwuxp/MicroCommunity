package com.java110.entity.attendance;

import com.alibaba.excel.annotation.ExcelProperty;

import java.io.Serializable;

/**
 * 个人加班
 * @author hh
 * create date 2021/9/24
 */
public class ImportOverTime implements Serializable {

    private static final long serialVersionUID = 7633613157315651028L;

    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty(index = 1)
    private String department;

    @ExcelProperty(index = 2)
    private String submitDate;

    @ExcelProperty(index = 3)
    private String startTime;

    @ExcelProperty(index = 4)
    private String endTime;

    @ExcelProperty(index = 5)
    private Integer workOvertimeDuration;

    @ExcelProperty(index = 6)
    private String workOvertimeMatter;

    @ExcelProperty(index = 7)
    private String approvalStatus;

    private Integer fileId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
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

    public Integer getWorkOvertimeDuration() {
        return workOvertimeDuration;
    }

    public void setWorkOvertimeDuration(Integer workOvertimeDuration) {
        this.workOvertimeDuration = workOvertimeDuration;
    }

    public String getWorkOvertimeMatter() {
        return workOvertimeMatter;
    }

    public void setWorkOvertimeMatter(String workOvertimeMatter) {
        this.workOvertimeMatter = workOvertimeMatter;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
}

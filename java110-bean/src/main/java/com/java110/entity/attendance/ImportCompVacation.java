package com.java110.entity.attendance;

import com.alibaba.excel.annotation.ExcelProperty;
import com.java110.entity.converter.ImportVacationRoadConverter;

import java.io.Serializable;

/**
 * @author huang.sheng
 * @date 2022/6/15
 */
public class ImportCompVacation implements Serializable {

    private static final long serialVersionUID = -89366078620803556L;

    @ExcelProperty(index = 0)
    private String name;

    @ExcelProperty(index = 1)
    private String department;

    // @ExcelProperty(index = 2)
    // private String position1;

    @ExcelProperty(index = 2)
    private String position;

    @ExcelProperty(index = 3)
    private String positionLevel;

    @ExcelProperty(index = 4)
    private String vacationType;

    // @ExcelProperty(index = 4)
    // private Double leaveDay;

    // @ExcelProperty(index = 5,converter = ImportVacationRoadConverter.class)
    // private String roadDay;

    // @ExcelProperty(index = 6)
    // private String returnTime;

    @ExcelProperty(index = 5)
    private String startTime;

    @ExcelProperty(index = 6)
    private String endTime;

    @ExcelProperty(index = 7)
    private String actStartTime;

    @ExcelProperty(index = 8)
    private String actEndTime;

    // @ExcelProperty(index = 11)
    // private String backType;

    @ExcelProperty(index = 9)
    private Double leaveDay;

    @ExcelProperty(index = 10)
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

    // public String getPosition1() {
    //     return position1;
    // }
    //
    // public void setPosition1(String position1) {
    //     this.position1 = position1;
    // }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getVacationType() {
        return vacationType;
    }

    public void setVacationType(String vacationType) {
        this.vacationType = vacationType;
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

    public String getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(String actStartTime) {
        this.actStartTime = actStartTime;
    }

    public String getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(String actEndTime) {
        this.actEndTime = actEndTime;
    }

    public Double getLeaveDay() {
        return leaveDay;
    }

    public void setLeaveDay(Double leaveDay) {
        this.leaveDay = leaveDay;
    }

    // public String getBackType() {
    //     return backType;
    // }
    //
    // public void setBackType(String backType) {
    //     this.backType = backType;
    // }

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

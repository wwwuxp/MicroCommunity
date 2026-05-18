package com.java110.po.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class AttendanceRecordPo {

    private Long personId;
    private String department;
    private String position;
    private String name;
    private String attendanceDate;
    private String actualStartWorkTime;
    private String actualEndWorkTime;
    private Integer type;
    private Integer repairType;
    private Date createTime;
    private String remark;

        public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getActualStartWorkTime() {
        return actualStartWorkTime;
    }

    public void setActualStartWorkTime(String actualStartWorkTime) {
        this.actualStartWorkTime = actualStartWorkTime;
    }

    public String getActualEndWorkTime() {
        return actualEndWorkTime;
    }

    public void setActualEndWorkTime(String actualEndWorkTime) {
        this.actualEndWorkTime = actualEndWorkTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRepairType() {
        return repairType;
    }

    public void setRepairType(Integer repairType) {
        this.repairType = repairType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

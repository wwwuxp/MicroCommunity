package com.java110.dto.keyCabinetLog;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * @date 2022/2/10
 */
public class KeyCabinetLogDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 1931337954283817156L;

    /**
     * 钥匙编号
     */
    private String keyNo;

    /**
     * 类型（1：取钥匙，2：还钥匙）
     */
    private Integer type;

    /**
     * 取还钥匙记录时间
     */
    private String recordTime;

    /**
     * 姓名
     */
    private String name;

    /**
     * 部门(科室)
     */
    private String department;

    private String userId;

    private String startTime;
    private String endTime;

    public String getKeyNo() {
        return keyNo;
    }

    public void setKeyNo(String keyNo) {
        this.keyNo = keyNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

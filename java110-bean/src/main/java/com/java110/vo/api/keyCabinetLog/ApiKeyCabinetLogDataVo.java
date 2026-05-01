package com.java110.vo.api.keyCabinetLog;

import java.io.Serializable;

/**
 * @author hh
 * @date 2022/2/14
 */
public class ApiKeyCabinetLogDataVo implements Serializable {

    private static final long serialVersionUID = -5619031305045357418L;

    private Integer id;
    private String recordTime;
    private Integer type;
    private String department;
    private String name;
    private String userId;
    private Integer identifyMode;
    private String keyNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getIdentifyMode() {
        return identifyMode;
    }

    public void setIdentifyMode(Integer identifyMode) {
        this.identifyMode = identifyMode;
    }

    public String getKeyNo() {
        return keyNo;
    }

    public void setKeyNo(String keyNo) {
        this.keyNo = keyNo;
    }
}

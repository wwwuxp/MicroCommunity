package com.java110.vo.api.passRecord;

import java.io.Serializable;

/**
 * @author hh
 * @date 2022/1/26
 */
public class ApiPassRecordDataVo implements Serializable {

    private static final long serialVersionUID = -448160091929536723L;

    private String name;
    private String department;
    private String position;
    private String deviceName;
    private String passTime;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

}

package com.java110.dto.user;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/13
 */
public class UserSimpleDto implements Serializable {

    private static final long serialVersionUID = 2937342196994959615L;

    private String userId;
    private String name;
    private String department;
    private String position;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

package com.java110.vo.api.outGoing;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/11/18
 */
public class ApiOutGoingPersonnel implements Serializable {

    private static final long serialVersionUID = 7754642714205250863L;

    private String userId;
    private String name;
    private String department;

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
}

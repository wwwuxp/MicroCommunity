package com.java110.dto.owner;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/8/5 10:12
 */
public class OwnerSimpleDto implements Serializable {

    private static final long serialVersionUID = -5197311511082569752L;

    private String userId;
    private String name;
    private String tel;

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}

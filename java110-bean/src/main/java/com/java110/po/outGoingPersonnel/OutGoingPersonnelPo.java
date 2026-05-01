package com.java110.po.outGoingPersonnel;

import java.io.Serializable;
import java.util.Date;

public class OutGoingPersonnelPo implements Serializable {

    private String personnelId;
    private String outId;
    private String statusCd = "0";
    private String userId;

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}

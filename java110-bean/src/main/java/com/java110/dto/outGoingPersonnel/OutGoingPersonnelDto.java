package com.java110.dto.outGoingPersonnel;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

public class OutGoingPersonnelDto extends PageDto implements Serializable {

    private String personnelId;
    private String outId;
    private String userId;


    private Date createTime;

    private String statusCd = "0";


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}

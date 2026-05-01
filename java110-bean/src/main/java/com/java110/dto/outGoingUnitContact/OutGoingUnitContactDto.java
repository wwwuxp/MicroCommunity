package com.java110.dto.outGoingUnitContact;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

public class OutGoingUnitContactDto extends PageDto implements Serializable {

    private String contactId;
    private String name;
    private String tel;
    private String outId;

    private Date createTime;

    private String statusCd = "0";


    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
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

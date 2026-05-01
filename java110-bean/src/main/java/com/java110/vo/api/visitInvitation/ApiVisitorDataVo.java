package com.java110.vo.api.visitInvitation;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/18
 */
public class ApiVisitorDataVo implements Serializable {

    private static final long serialVersionUID = 5476993582436919964L;

    private String visitorId;
    private Integer no;
    private String name;
    private String tel;
    private String unit;
    private String idCard;
    private String license;

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}

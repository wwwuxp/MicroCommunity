package com.java110.dto.pubCarDriver;

import java.io.Serializable;

/**
 * 检验是否存在相同司机类
 * @author hh
 * @datetime 2021/8/27 11:28
 */
public class PubCarExistDriverDto implements Serializable {

    private static final long serialVersionUID = -4159714215974596485L;

    private String driverId;
    private String jobNum;
    private String tel;
    private String staffCarNum;
    private String idNum;
    private String communityId;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStaffCarNum() {
        return staffCarNum;
    }

    public void setStaffCarNum(String staffCarNum) {
        this.staffCarNum = staffCarNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}

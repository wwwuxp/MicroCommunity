package com.java110.vo.api.pubCarTrip;

import java.io.Serializable;

public class ApiSimpleTripDataVo extends BaseStatisticsDataVo implements Serializable {

    private Integer applyNum;
    private String license;

    public Integer getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(Integer applyNum) {
        this.applyNum = applyNum;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}

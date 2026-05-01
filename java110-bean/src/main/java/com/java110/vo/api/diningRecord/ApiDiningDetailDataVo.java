package com.java110.vo.api.diningRecord;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/11/12
 */
public class ApiDiningDetailDataVo implements Serializable {

    private static final long serialVersionUID = 7286460666056691144L;

    private String diningDate;
    private Integer diningCount;
    private String morningDiningTime;
    private String noonDiningTime;
    private String afternoonDiningTime;

    private String diningTime;

    public String getDiningDate() {
        return diningDate;
    }

    public void setDiningDate(String diningDate) {
        this.diningDate = diningDate;
    }

    public Integer getDiningCount() {
        return diningCount;
    }

    public void setDiningCount(Integer diningCount) {
        this.diningCount = diningCount;
    }

    public String getMorningDiningTime() {
        return morningDiningTime;
    }

    public void setMorningDiningTime(String morningDiningTime) {
        this.morningDiningTime = morningDiningTime;
    }

    public String getNoonDiningTime() {
        return noonDiningTime;
    }

    public void setNoonDiningTime(String noonDiningTime) {
        this.noonDiningTime = noonDiningTime;
    }

    public String getAfternoonDiningTime() {
        return afternoonDiningTime;
    }

    public void setAfternoonDiningTime(String afternoonDiningTime) {
        this.afternoonDiningTime = afternoonDiningTime;
    }

    public String getDiningTime() {
        return diningTime;
    }

    public void setDiningTime(String diningTime) {
        this.diningTime = diningTime;
    }
}

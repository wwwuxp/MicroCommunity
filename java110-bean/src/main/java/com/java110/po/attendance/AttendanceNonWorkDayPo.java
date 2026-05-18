package com.java110.po.attendance;

import java.io.Serializable;

/**
 * @author lmf
 * 非工作日实体类
 * @date 2022/6/30
 */
public class AttendanceNonWorkDayPo implements Serializable {
    private static final long serialVersionUID = 2072009777085101328L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 年月
     */
    private String yearMonth;

    private String startDate;

    private String endDate;

    /**
     * 节假日标志
     */
    private String holidayFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHolidayFlag() {
        return holidayFlag;
    }

    public void setHolidayFlag(String holidayFlag) {
        this.holidayFlag = holidayFlag;
    }
}

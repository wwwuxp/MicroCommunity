package com.java110.dto.pubCarUser;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hh
 * @datetime 2021/8/3 16:33
 */
public class DepartmentStatisticsDto extends PageDto implements Serializable {

    /**
     * 部门
     */
    private String department;

    /**
     * 出发时间-小
     */
    private Date lessStartTime;

    /**
     * 出发时间-大
     */
    private Date moreStartTime;

    /**
     * 结束时间-小
     */
    private Date lessEndTime;

    /**
     * 结束时间-大
     */
    private Date moreEndTime;

    /**
     * 商户id
     */
    private String storeId;

    private Date startTime;
    private Date endTime;

    /**
     * 小区id
     */
    private String communityId;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getMoreStartTime() {
        return moreStartTime;
    }

    public void setMoreStartTime(Date moreStartTime) {
        this.moreStartTime = moreStartTime;
    }

    public Date getLessEndTime() {
        return lessEndTime;
    }

    public void setLessEndTime(Date lessEndTime) {
        this.lessEndTime = lessEndTime;
    }

    public Date getMoreEndTime() {
        return moreEndTime;
    }

    public void setMoreEndTime(Date moreEndTime) {
        this.moreEndTime = moreEndTime;
    }

    public Date getLessStartTime() {
        return lessStartTime;
    }

    public void setLessStartTime(Date lessStartTime) {
        this.lessStartTime = lessStartTime;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

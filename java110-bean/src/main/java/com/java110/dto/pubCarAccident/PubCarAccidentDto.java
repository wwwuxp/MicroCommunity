package com.java110.dto.pubCarAccident;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/11
 */
public class PubCarAccidentDto extends PageDto implements Serializable {

    private static final long serialVersionUID = -5235048300938649577L;

    private String lessCarUseDate;
    private String moreCarUseDate;
    private String department;
    private String position;
    private String name;
    private String communityId;

    public String getLessCarUseDate() {
        return lessCarUseDate;
    }

    public void setLessCarUseDate(String lessCarUseDate) {
        this.lessCarUseDate = lessCarUseDate;
    }

    public String getMoreCarUseDate() {
        return moreCarUseDate;
    }

    public void setMoreCarUseDate(String moreCarUseDate) {
        this.moreCarUseDate = moreCarUseDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}

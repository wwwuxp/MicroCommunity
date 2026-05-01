package com.java110.dto.inspectionPlan;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author hh
 * create date 2021/9/18
 */
public class InspectionPositionDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 4916160846902049380L;

    /**
     * 巡检路线名称
     */
    private String routeName;

    /**
     * 巡检点名称
     */
    private String inspectionName;

    private String communityId;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }
}

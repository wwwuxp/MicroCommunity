package com.java110.po.pubCarApprovalProcess;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/12/6
 */
public class PubCarApprovalProcessPo implements Serializable {

    private static final long serialVersionUID = -1787412390128761386L;

    /**
     * 用车
     */
    public static final int TYPE_PUBCAR = 1000;

    /**
     * 外出
     */
    public static final int TYPE_OUTGOING = 2000;

    /**
     * 局负责人
     */
    public static final int OUTGOING_BUREAU_HEADER = 1;

    /**
     * 外出局领导
     */
    public static final int OUTGOING_BUREAU_LEADER = 2;

    /**
     * 公车局领导
     */
    public static final int PUBCAR_BUREAU_LEADER = 1;

    /**
     * 用车管理员
     */
    public static final int PUBCAR_ADMINISTRATOR = 2;

    /**
     * 部门领导
     */
    public static final int DEPARTMENT_LEADER = 3;

    /**
     * 修车审批人
     */
    public static final Integer VEHICLE_REPAIR = 4;

    /**
     * 普通员工
     */
    public static final int GENERAL_STAFF = 4;

    private Integer id;
    private String tripId;
    private String userId;
    private Integer approvalStatus;
    private Integer order;
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

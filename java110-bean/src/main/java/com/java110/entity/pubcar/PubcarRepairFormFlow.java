package com.java110.entity.pubcar;


import java.io.Serializable;
import java.util.Date;

/**
 * (PubcarRepairFormFlow)表实体类
 *
 * @author lmf
 * @since 2022-09-21 09:07:19
 */
public class PubcarRepairFormFlow implements Serializable {

    private static final long serialVersionUID = 3645092372704401756L;

    public PubcarRepairFormFlow() {
    }

    public PubcarRepairFormFlow(String repairId) {
        this.repairId = repairId;
    }
    public PubcarRepairFormFlow(String repairId, String userId, String userName) {
        this.repairId = repairId;
        this.userId = userId;
        this.userName = userName;
    }

    public PubcarRepairFormFlow(String flowId, String repairId, String userId, String userName, String status, String flowName, String remark, Integer index) {
        this.flowId = flowId;
        this.repairId = repairId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.remark = remark;
        this.index = index;
        this.flowName = flowName;
    }

    public PubcarRepairFormFlow(String repairId, String userId, String status, String remark) {
        this.repairId = repairId;
        this.userId = userId;
        this.status = status;
        this.remark = remark;
    }

    /**
     * 主键
     */
    private String flowId;
    /**
     * 修车审批表数据id
     */
    private String repairId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 审批状态
     */
    private String status;
    /**
     * 步骤名称
     */
    private String flowName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 顺序
     */
    private Integer index;

    /**
     * 流程步长 即现在第几步
     */
    private Integer flowStep;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFlowStep() {
        return flowStep;
    }

    public void setFlowStep(Integer flowStep) {
        this.flowStep = flowStep;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getRepairId() {
        return repairId;
    }

    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}

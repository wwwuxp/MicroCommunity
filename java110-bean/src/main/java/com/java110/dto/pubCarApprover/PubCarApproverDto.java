package com.java110.dto.pubCarApprover;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/12/13
 */
public class PubCarApproverDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 4008798035251814427L;

    private Integer id;
    private List<String> approverIds;
    private String approverId;
    private Integer roleType;
    private Integer tableType;
    private String departmentId;
    private String communityId;
    private String statusCd = "0";
    private String name;
    private String approvalTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getApproverIds() {
        return approverIds;
    }

    public void setApproverIds(List<String> approverIds) {
        this.approverIds = approverIds;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }
}

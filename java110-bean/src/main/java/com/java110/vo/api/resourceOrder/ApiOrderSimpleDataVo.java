package com.java110.vo.api.resourceOrder;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/17
 */
public class ApiOrderSimpleDataVo implements Serializable {

    private static final long serialVersionUID = 8293378084279819072L;

    private String applyOrderId;
    private String taskId;

    private List<ApiResourceSimpleDataVo> resources;

    private String userName;
    private String departPosition;
    private String tel;
    private String state;
    private String resOrderType;

    private String description;

    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }

    public List<ApiResourceSimpleDataVo> getResources() {
        return resources;
    }

    public void setResources(List<ApiResourceSimpleDataVo> resources) {
        this.resources = resources;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartPosition() {
        return departPosition;
    }

    public void setDepartPosition(String departPosition) {
        this.departPosition = departPosition;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResOrderType() {
        return resOrderType;
    }

    public void setResOrderType(String resOrderType) {
        this.resOrderType = resOrderType;
    }
}

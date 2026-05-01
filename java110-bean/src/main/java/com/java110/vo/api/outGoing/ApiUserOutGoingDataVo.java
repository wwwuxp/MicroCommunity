package com.java110.vo.api.outGoing;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/11/18
 */
public class ApiUserOutGoingDataVo implements Serializable {

    private static final long serialVersionUID = 2463671982035419147L;

    private String outId;
    private String startTime;
    private String endTime;
    private String matterDetail;
    private String destination;
    private Integer state;

    private List<ApiOutGoingPersonnel> personnels;

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMatterDetail() {
        return matterDetail;
    }

    public void setMatterDetail(String matterDetail) {
        this.matterDetail = matterDetail;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<ApiOutGoingPersonnel> getPersonnels() {
        return personnels;
    }

    public void setPersonnels(List<ApiOutGoingPersonnel> personnels) {
        this.personnels = personnels;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}

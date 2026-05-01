package com.java110.vo.api.pubCarAccident;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/10/11
 */
public class ApiPubCarAccidentDataVo implements Serializable {

    private static final long serialVersionUID = 6537522899044533546L;

    private String name;
    private String department;
    private String position;
    private String carUseDate;
    private String start;
    private String destination;
    private String startTime;
    private String endTime;
    private String accidentSceneImg;
    private String responsibilityConfirmImg;
    private String[] accidentSceneImgs;
    private String[] responsibilityConfirmImgs;
    private String detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCarUseDate() {
        return carUseDate;
    }

    public void setCarUseDate(String carUseDate) {
        this.carUseDate = carUseDate;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public String[] getAccidentSceneImgs() {
        return accidentSceneImgs;
    }

    public void setAccidentSceneImgs(String[] accidentSceneImgs) {
        this.accidentSceneImgs = accidentSceneImgs;
    }

    public String[] getResponsibilityConfirmImgs() {
        return responsibilityConfirmImgs;
    }

    public void setResponsibilityConfirmImgs(String[] responsibilityConfirmImgs) {
        this.responsibilityConfirmImgs = responsibilityConfirmImgs;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAccidentSceneImg() {
        return accidentSceneImg;
    }

    public void setAccidentSceneImg(String accidentSceneImg) {
        this.accidentSceneImg = accidentSceneImg;
    }

    public String getResponsibilityConfirmImg() {
        return responsibilityConfirmImg;
    }

    public void setResponsibilityConfirmImg(String responsibilityConfirmImg) {
        this.responsibilityConfirmImg = responsibilityConfirmImg;
    }
}

package com.java110.vo.api.pubCar;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hh
 * @datetime 2021/7/21 16:13
 */
public class ApiPubCarAttrDataVo implements Serializable {

    private String attrId;
    private String registerDate;
    private String engineNum;
    private String frameNum;
    private Integer accidentSituation;
    private String accidentSituationName;
    private String groupId;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getFrameNum() {
        return frameNum;
    }

    public void setFrameNum(String frameNum) {
        this.frameNum = frameNum;
    }

    public Integer getAccidentSituation() {
        return accidentSituation;
    }

    public void setAccidentSituation(Integer accidentSituation) {
        this.accidentSituation = accidentSituation;
    }

    public String getAccidentSituationName() {
        return accidentSituationName;
    }

    public void setAccidentSituationName(String accidentSituationName) {
        this.accidentSituationName = accidentSituationName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

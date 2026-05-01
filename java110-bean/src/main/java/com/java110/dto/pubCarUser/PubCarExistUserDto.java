package com.java110.dto.pubCarUser;

import java.io.Serializable;
import java.util.List;

/**
 * 用车人是否存在实体类
 * @author hh
 * @datetime 2021/8/27 13:44
 */
public class PubCarExistUserDto implements Serializable {

    private static final long serialVersionUID = -1856177394873992203L;

    private String carUserId;
    private String userId;
    private Integer isSelfDriving;
    private String communityId;

    private List<String> userIds;

    public String getCarUserId() {
        return carUserId;
    }

    public void setCarUserId(String carUserId) {
        this.carUserId = carUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getIsSelfDriving() {
        return isSelfDriving;
    }

    public void setIsSelfDriving(Integer isSelfDriving) {
        this.isSelfDriving = isSelfDriving;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}

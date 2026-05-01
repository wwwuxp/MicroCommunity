package com.java110.dto.pubCar;

import java.io.Serializable;

/**
 * 检验是否存在相同车辆的实体类
 * @author hh
 * @datetime 2021/8/27 11:53
 */
public class ExistPubCarDto implements Serializable {

    private static final long serialVersionUID = -6955249830283607016L;

    private String carId;
    private String communityId;
    private String license;
    private String keyCabinet;
    private String snId;

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getKeyCabinet() {
        return keyCabinet;
    }

    public void setKeyCabinet(String keyCabinet) {
        this.keyCabinet = keyCabinet;
    }
}

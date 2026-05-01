package com.java110.dto.tablePicture;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2022/1/12
 */
public class TablePictureDto implements Serializable {

    private static final long serialVersionUID = 9209692106401078691L;

    /**
     * 维修图片
     */
    public static final int REPAIR = 1;

    /**
     * 维修前图片
     */
    public static final int BEFORE_REPAIR = 2;

    /**
     * 维修后图片
     */
    public static final int AFTER_REPAIR = 3;

    /**
     * 员工人脸照片
     */
    public static final int USER_FACE = 1;

    private Integer id;
    private JSONArray pictures;
    private byte[] picture;
    private List<String> priKeys;
    private String priKey;
    private String tableName;
    private Integer pictureType;
    private Boolean isFace;
    private byte[] featureCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JSONArray getPictures() {
        return pictures;
    }

    public void setPictures(JSONArray pictures) {
        this.pictures = pictures;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<String> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<String> priKeys) {
        this.priKeys = priKeys;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getPictureType() {
        return pictureType;
    }

    public void setPictureType(Integer pictureType) {
        this.pictureType = pictureType;
    }

    public Boolean getFace() {
        return isFace;
    }

    public void setFace(Boolean face) {
        isFace = face;
    }

    public byte[] getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(byte[] featureCode) {
        this.featureCode = featureCode;
    }
}

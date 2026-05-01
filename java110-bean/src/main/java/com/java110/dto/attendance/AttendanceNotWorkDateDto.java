package com.java110.dto.attendance;

import com.java110.dto.PageDto;

import java.io.Serializable;

/**
 * @author lmf
 * 非工作日数据传输类
 * @date 2022/6/28
 */
public class AttendanceNotWorkDateDto extends PageDto implements Serializable {

    private static final long serialVersionUID = 8551846412435007506L;

    /**
     * 主键
     */
    private String id;
    /**
     * 日期本体
     */
    private String content;
    /**
     * 年份
     */
    private String year;
    /**
     * 年份开始
     */
    private String startYear;
    /**
     * 年份结束
     */
    private String endYear;
    /**
     * 类型。1：非工作日，2：节假日，3：其它
     */
    private Integer type;
    /**
     * 商户id 预留字段
     */
    private String storeId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 删除标识
     */
    private String deletedFlag;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }
}

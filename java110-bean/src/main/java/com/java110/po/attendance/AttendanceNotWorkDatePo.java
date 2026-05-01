package com.java110.po.attendance;

import java.io.Serializable;

/**
 * @author lmf
 * 非工作日实体类
 * @date 2022/6/30
 */
public class AttendanceNotWorkDatePo implements Serializable {
    private static final long serialVersionUID = 2072009777085101328L;
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

    public String getDeleteFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
}

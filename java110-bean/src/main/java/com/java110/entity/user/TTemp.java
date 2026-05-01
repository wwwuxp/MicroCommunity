package com.java110.entity.user;


import java.io.Serializable;

/**
 * (TTemp)表实体类
 * 模板表，怎么使用看自己
 *
 * @author lmf
 * @since 2022-09-20 18:46:55
 */
public class TTemp implements Serializable {

    public TTemp() {
    }

    public TTemp(String tempKey) {
        this.tempKey = tempKey;
        this.statusCd = "0";
    }

    /**
     * 主键自增 无意义
     */
    private Long tempId;
    /**
     * 模板名称
     */
    private String tempName;
    /**
     * 模板键值
     */
    private String tempKey;
    /**
     * 模板值，建议json类型
     */
    private String tempValue;
    /**
     * 模板说明
     */
    private String tempRemark;
    /**
     * 数据状态，详细参考c_status表，S 保存，0, 在用 1失效
     */
    private String statusCd;

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getTempKey() {
        return tempKey;
    }

    public void setTempKey(String tempKey) {
        this.tempKey = tempKey;
    }

    public String getTempValue() {
        return tempValue;
    }

    public void setTempValue(String tempValue) {
        this.tempValue = tempValue;
    }

    public String getTempRemark() {
        return tempRemark;
    }

    public void setTempRemark(String tempRemark) {
        this.tempRemark = tempRemark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}

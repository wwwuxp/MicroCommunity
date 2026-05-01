package com.java110.vo.api.pubCarReimburse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author hh
 * @date 2021/10/12
 */
public class ApiPubCarReimburseDataVo implements Serializable {

    private static final long serialVersionUID = 7955347469867681848L;

    private String userId;
    private String name;
    private String department;
    private String position;
    private String reimburseMonth;
    private BigDecimal totalAmount;

    private List<ReimburseDetailInfo> reimburses;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getReimburseMonth() {
        return reimburseMonth;
    }

    public void setReimburseMonth(String reimburseMonth) {
        this.reimburseMonth = reimburseMonth;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ReimburseDetailInfo> getReimburses() {
        return reimburses;
    }

    public void setReimburses(List<ReimburseDetailInfo> reimburses) {
        this.reimburses = reimburses;
    }
}

package com.java110.ext.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工Excel导出导入实体
 */
public class StaffExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("员工ID")
    @ColumnWidth(20)
    private String staffId;

    @ExcelProperty("商户ID")
    @ColumnWidth(20)
    private String storeId;

    @ExcelProperty("员工姓名")
    @ColumnWidth(12)
    private String staffName;

    @ExcelProperty("员工编码")
    @ColumnWidth(15)
    private String staffCode;

    @ExcelProperty("手机号")
    @ColumnWidth(15)
    private String tel;

    @ExcelProperty("身份证号")
    @ColumnWidth(20)
    private String idCard;

    @ExcelProperty("性别")
    @ColumnWidth(8)
    private String sex;

    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    private String email;

    @ExcelProperty("地址")
    @ColumnWidth(30)
    private String address;

    @ExcelProperty("入职日期")
    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    private Date joinDate;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String statusCd;

    @ExcelProperty("来源系统")
    @ColumnWidth(15)
    private String sourceSystem;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
}

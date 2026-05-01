package com.java110.ext.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织Excel导出导入实体
 */
public class OrgExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("组织ID")
    @ColumnWidth(20)
    private String orgId;

    @ExcelProperty("商户ID")
    @ColumnWidth(20)
    private String storeId;

    @ExcelProperty("组织名称")
    @ColumnWidth(20)
    private String orgName;

    @ExcelProperty("组织层级")
    @ColumnWidth(10)
    private String orgLevel;

    @ExcelProperty("父组织ID")
    @ColumnWidth(20)
    private String parentOrgId;

    @ExcelProperty("描述")
    @ColumnWidth(30)
    private String description;

    @ExcelProperty("所属小区ID")
    @ColumnWidth(20)
    private String belongCommunityId;

    @ExcelProperty("状态")
    @ColumnWidth(10)
    private String statusCd;

    @ExcelProperty("来源系统")
    @ColumnWidth(15)
    private String sourceSystem;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBelongCommunityId() {
        return belongCommunityId;
    }

    public void setBelongCommunityId(String belongCommunityId) {
        this.belongCommunityId = belongCommunityId;
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

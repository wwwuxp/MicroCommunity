package com.java110.ext.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工组织关系Excel导出导入实体
 */
@Data
public class OrgStaffRelExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("用户id")
    @ColumnWidth(20)
    private String userId;

    @ExcelProperty("用户名称")
    @ColumnWidth(20)
    private String userName;

    @ExcelProperty("用户邮箱")
    @ColumnWidth(20)
    private String email;

    @ExcelProperty("用户地址")
    @ColumnWidth(20)
    private String address;

    @ExcelProperty("用户性别")
    @ColumnWidth(20)
    private String sex;

    @ExcelProperty("用户电话")
    @ColumnWidth(20)
    private String tel;

    @ExcelProperty("用户工号")
    @ColumnWidth(20)
    private String userNum;

    @ExcelProperty("用户短id")
    @ColumnWidth(20)
    private String invitorId;

    @ExcelProperty("卡号")
    @ColumnWidth(20)
    private String cardNumber;

    @ExcelProperty("用户来源")
    @ColumnWidth(20)
    private String flag;

    @ExcelProperty("数据来源id")
    @ColumnWidth(20)
    private String sourceUserId;

    @ExcelProperty("关联id")
    @ColumnWidth(20)
    private String relId;

    @ExcelProperty("岗位")
    @ColumnWidth(20)
    private String relCd;

    @ExcelProperty("岗位名称")
    @ColumnWidth(20)
    private String relCdName;

    @ExcelProperty("商户id")
    @ColumnWidth(20)
    private String storeId;

    @ExcelProperty("关联来源")
    @ColumnWidth(20)
    private String relFlag;

    @ExcelProperty("组织ID")
    @ColumnWidth(20)
    private String orgId;

    @ExcelProperty("组织名称")
    @ColumnWidth(20)
    private String orgName;

    @ExcelProperty("组织级别")
    @ColumnWidth(10)
    private String orgLevel;

    @ExcelProperty("父组织id")
    @ColumnWidth(20)
    private String parentOrgId;

    @ExcelProperty("组织描述")
    @ColumnWidth(30)
    private String description;

    @ExcelProperty("组织小区id")
    @ColumnWidth(10)
    private String belongCommunityId;

    @ExcelProperty("操作权限")
    @ColumnWidth(10)
    private String allowOperation;

    @ExcelProperty("组织来源")
    @ColumnWidth(10)
    private String orgFlag;

    @ExcelProperty("组织来源id")
    @ColumnWidth(20)
    private String sourceOrgId;

}

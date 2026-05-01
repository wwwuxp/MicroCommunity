package com.java110.ext.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 员工组织关系实体
 * 对应表: pm_org_staff_rel
 */
@Data
@TableName("u_org_staff_rel")
public class OrgStaffRelPo {

    /**
     * 关系ID
     */
    @TableId(type = IdType.INPUT)
    private String relId;

    /**
     * 组织ID
     */
    private String orgId;

    private String bId;

    /**
     * 员工ID
     */
    private String staffId;

    /**
     * 关系编码
     */
    private String relCd;

    /**
     * 商户ID
     */
    private String storeId;

    /**
     * 状态：0-有效，1-无效
     */
    private String statusCd = "0";

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 数据来源标识
     */
    private String flag;

    private Date syncTime;
}

package com.java110.ext.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 组织/部门实体
 * 对应表: pm_org
 */
@Data
@TableName("u_org")
public class OrgPo {

    /**
     * 组织ID
     */
    @TableId(type = IdType.INPUT)
    private String orgId;

    /**
     * 商户ID
     */
    private String storeId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织层级
     */
    private String orgLevel;

    /**
     * 父组织ID
     */
    private String parentOrgId;

    /**
     * 描述
     */
    private String description;

    /**
     * 所属小区ID
     */
    private String belongCommunityId;


    /**
     * 状态：0-有效，1-无效
     */
    private String statusCd = "0";

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 最后同步时间
     */
    private Date syncTime;

    /**
     * 数据源id
     */
    private String sourceOrgId;

    /**
     * 允许前台操作数据，T可以，F不可以
     */
    private String allowOperation;

    /**
     * 标记。1：数据来自政务微信；null则是系统自添加；2：从数字化平台同步
     */
    private Integer flag;
}

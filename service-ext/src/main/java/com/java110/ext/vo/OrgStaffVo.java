package com.java110.ext.vo;

import lombok.Data;

@Data
public class OrgStaffVo {

    //用户id
    private String userId;
    //用户名字
    private String userName;
    //用户来源id
    private String userSourceId;

    private String userFlag;

    //部门id
    private String orgId;
    //部门名称
    private String orgName;
    //部门来源id
    private String orgSourceId;

    private String relId;

    private String relCd;

}

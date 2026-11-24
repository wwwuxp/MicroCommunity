package com.java110.ext.pojo.attendance;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_user")
public class SysUserPo {

    private String id;
    private String sourceUserId;
    private String username;
    private String realname;
    private String phone;
    private Date syncTime;
    private Integer delFlag;
    private String post;
}

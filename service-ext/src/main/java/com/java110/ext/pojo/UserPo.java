package com.java110.ext.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 13:03
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
@Data
@TableName("u_user")
public class UserPo implements Serializable {
    @TableId(type = IdType.INPUT)
    private String userId;
    private String name;
    private String email;
    private String address;
    private String locationCd;
    private String age;
    private String sex;
    private String tel;
    private String levelCd;
    // new add
    private String jobNum;
    private String cardNumber;
    private String statusCd;

    private String invitorId;
    private String sourceUserId;
    private String bId;
    private Date syncDate;

    private String password;

    private Date createTime;

    private String flag;
    private String personId;
}

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
@TableName("p_privilege_user")
public class PrivilegeUserPo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer puId;
    private String storeId;
    private String pId;
    private String userId;
    private String statusCd;
    private Date createTime;
    private String privilege_flag;
}

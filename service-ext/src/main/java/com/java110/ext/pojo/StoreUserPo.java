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
@TableName("s_store_user")
public class StoreUserPo implements Serializable {
    @TableId(type = IdType.INPUT)
    private String storeUserId;
    private String storeId;
    private String bId;
    private String userId;
    private String relCd;
    private String statusCd;
    private Date createTime;
    private String flag;
}

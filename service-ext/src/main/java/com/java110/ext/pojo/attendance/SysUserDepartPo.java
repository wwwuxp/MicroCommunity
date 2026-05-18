package com.java110.ext.pojo.attendance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user_depart")
public class SysUserDepartPo {

    @TableId(type = IdType.INPUT)
    private String id;

    private String userId;

    private String depId;
}

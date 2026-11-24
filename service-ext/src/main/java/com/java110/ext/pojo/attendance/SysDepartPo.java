package com.java110.ext.pojo.attendance;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_depart")
public class SysDepartPo {

    private String id;

    private String departName;

    private String delFlag;

    private String deptId;
}

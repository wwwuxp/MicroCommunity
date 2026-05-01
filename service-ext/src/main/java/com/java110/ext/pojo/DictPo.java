package com.java110.ext.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_dict")
public class DictPo {
    @TableId(type = IdType.INPUT)
    private String id;
    private String statusCd;

    private String name;

    private String description;

    private Date createTime;

    private String tableName;

    private String tableColumns;
}

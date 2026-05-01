package com.java110.ext.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 同步任务日志
 */
@Data
@TableName("ext_sync_task_log")
public class SyncTaskLogPo {

    /**
     * 任务ID
     */
    @TableId(type = IdType.INPUT)
    private String taskId;

    /**
     * 任务类型：ORG-组织/部门，STAFF-员工，REL-关系，ALL-全部
     */
    private String taskType;

    /**
     * 数据来源标识
     */
    private String sourceSystem;

    /**
     * 调用方式：AUTO-自动，TASK-定时任务，MANUAL-手动
     */
    private String invokeType;

    /**
     * 执行状态：RUNNING-执行中，SUCCESS-成功，FAIL-失败
     */
    private String status;

    /**
     * 同步组织数量
     */
    private Integer orgCount;

    /**
     * 同步员工数量
     */
    private Integer staffCount;

    /**
     * 同步关系数量
     */
    private Integer relCount;

    /**
     * 失败数量
     */
    private Integer failCount;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 耗时（毫秒）
     */
    private Long costTime;

    /**
     * 创建时间
     */
    private Date createTime;
}

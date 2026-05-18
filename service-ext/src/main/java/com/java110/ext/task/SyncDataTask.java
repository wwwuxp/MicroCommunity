package com.java110.ext.task;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.java110.ext.service.ISyncDataService;

/**
 * 用户体系数据同步定时任务
 * 统一同步组织、员工、组织员工关系数据
 */
@Component
public class SyncDataTask {

    private static final Logger logger = LoggerFactory.getLogger(SyncDataTask.class);

    @Autowired
    private ISyncDataService syncDataService;

    /**
     * 是否启用定时同步
     */
    @Value("${sync.task.enabled:true}")
    private boolean syncTaskEnabled;

    @Value("${sync.task.jyw:false}")
    private boolean syncTaskJyw;

    /**
     * 定时同步全部数据（组织+员工+关系）
     * 默认每天凌晨2点执行
     */
    @Scheduled(cron = "${sync.task.cron:0 0 2 * * ?}")
    public void syncAllData() {
        if (!syncTaskEnabled) {
            logger.info("定时同步任务未启用，跳过执行");
            return;
        }

        if (!syncTaskJyw) {
            logger.info("【定时任务】开始同步用户体系数据");
            try {
                Map<String, Object> result = syncDataService.syncAllData();
                logger.info("【定时任务】用户体系数据同步完成");
            } catch (Exception e) {
                logger.error("【定时任务】用户体系数据同步失败", e);
            }
        }
    }

    /**
     * 定时同步局域网系统，同步数据到
     * 默认每天凌晨3点执行
     */
    @Scheduled(cron = "${sync.task.cron:0 0 3 * * ?}")
    public void syncAttendanceUserDepart() {
        if (!syncTaskEnabled) {
            logger.info("syncAttendanceUserDepart 定时同步任务未启用，跳过执行");
            return;
        }

        if (syncTaskJyw) {
            logger.info("【syncAttendanceUserDepart】开始同步用户部门数据");
            try {
                syncDataService.writeToAttendanceSys();
            } catch (Exception e) {
                logger.error("【syncAttendanceUserDepart】用户部门数据同步失败", e);
            }
        }
    }
}

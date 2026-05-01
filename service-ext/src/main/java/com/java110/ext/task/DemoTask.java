//package com.java110.ext.task;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 示例定时任务
// * 注意：需要在启动类添加 @EnableScheduling 注解
// */
//@Component
//public class DemoTask {
//
//    private static final Logger logger = LoggerFactory.getLogger(DemoTask.class);
//
//    /**
//     * 每分钟执行一次
//     * cron表达式：秒 分 时 日 月 周
//     */
//    @Scheduled(cron = "0 * * * * ?")
//    public void executeTask1() {
//        logger.info("DemoTask executeTask1 执行定时任务，每分钟执行");
//        // TODO: 添加业务逻辑
//    }
//
//    /**
//     * 每小时执行一次
//     */
//    @Scheduled(cron = "0 0 * * * ?")
//    public void executeTask2() {
//        logger.info("DemoTask executeTask2 执行定时任务，每小时执行");
//        // TODO: 添加业务逻辑
//    }
//
//    /**
//     * 每天凌晨1点执行一次
//     */
//    @Scheduled(cron = "0 0 1 * * ?")
//    public void executeTask3() {
//        logger.info("DemoTask executeTask3 执行定时任务，每天凌晨1点执行");
//        // TODO: 添加业务逻辑
//    }
//
//    /**
//     * 每隔5秒执行一次（固定间隔）
//     */
//    @Scheduled(fixedRate = 5000)
//    public void executeTask4() {
//        logger.info("DemoTask executeTask4 执行定时任务，每隔5秒执行");
//        // TODO: 添加业务逻辑
//    }
//}

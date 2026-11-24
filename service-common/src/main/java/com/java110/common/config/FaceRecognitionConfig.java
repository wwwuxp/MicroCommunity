//package com.java110.common.config;
//
//import com.arcsoft.face.ActiveFileInfo;
//import com.arcsoft.face.EngineConfiguration;
//import com.arcsoft.face.FaceEngine;
//import com.arcsoft.face.FunctionConfiguration;
//import com.arcsoft.face.enums.DetectMode;
//import com.arcsoft.face.enums.DetectOrient;
//import com.arcsoft.face.enums.ErrorInfo;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.File;
//import java.util.Objects;
//
///**
// * 人脸识别配置
// * @author hh
// * @date 2022/1/20
// */
//@Configuration
//public class FaceRecognitionConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(FaceRecognitionConfig.class);
//
//    @Bean
//    public FaceEngine faceEngine(){
//        // 访客机面板APP_ID
//        String appkey = "GoUNzXoHcYWAxc7thsiNQYZMMYxdm87eVEjzxyLr3RQ5";
//        // SDK_KEY(LINUX版本)
//        String appSecret = "4XZ37QeAY1rPsQWbygX5tnC3nbwtFTkEGa6nAQL1jU4L";
//        // SDK_KEY(WINDOWS版本)
//        // String appSecret = "4XZ37QeAY1rPsQWbygX5tnC3viWxugdvTYsVHtbkteUq";
//        String libPath = getLibPath();
//        logger.error("获取到的libPath是：{}", libPath);
//        FaceEngine faceEngine = new FaceEngine(libPath);
//
//        int activeOnline = faceEngine.activeOnline(appkey, appSecret);
//        if (activeOnline != ErrorInfo.MOK.getValue() && activeOnline != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
//            logger.error("引擎激活失败");
//        }
//        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
//        int activeFileInfo1 = faceEngine.getActiveFileInfo(activeFileInfo);
//        if (activeFileInfo1 != ErrorInfo.MOK.getValue() && activeFileInfo1 != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
//            logger.error("获取激活文件信息失败");
//        }
//
//        // 引擎配置
//        EngineConfiguration engineConfiguration = new EngineConfiguration();
//        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
//        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);
//        engineConfiguration.setDetectFaceMaxNum(10);
//        engineConfiguration.setDetectFaceScaleVal(16);
//
//        // 功能配置
//        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
//        functionConfiguration.setSupportAge(true);
//        functionConfiguration.setSupportFace3dAngle(true);
//        functionConfiguration.setSupportFaceDetect(true);
//        functionConfiguration.setSupportFaceRecognition(true);
//        functionConfiguration.setSupportGender(true);
//        functionConfiguration.setSupportLiveness(true);
//        functionConfiguration.setSupportIRLiveness(true);
//        engineConfiguration.setFunctionConfiguration(functionConfiguration);
//
//        // 初始化引擎
//        int errorCode = faceEngine.init(engineConfiguration);
//        if (errorCode != ErrorInfo.MOK.getValue()) {
//            logger.error("==============初始化引擎失败==============");
//        }else {
//            logger.error("==============初始化引擎成功==============");
//        }
//        return faceEngine;
//
//    }
//
//    private String getLibPath() {
//        String libPath;
//        String os = System.getProperty("os.name");
//        if (Objects.nonNull(os) && os.toLowerCase().startsWith("win")){
//            libPath = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "WIN64";
//        }else {
//            libPath = "/root/LINUX64";
//        }
//        return libPath;
//    }
//
//}

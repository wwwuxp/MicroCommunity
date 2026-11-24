package com.java110.common.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.java110.common.dao.ITablePictureServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.tablePicture.TablePictureDto;
import com.java110.intf.common.ITablePictureInnerServiceSMO;
import com.java110.utils.util.Base64Convert;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hh
 * @date 2022/1/12
 */
@RestController
public class TablePictureInnerServiceSMOImpl extends BaseServiceSMO implements ITablePictureInnerServiceSMO {

    private static final Logger logger = LoggerFactory.getLogger(TablePictureInnerServiceSMOImpl.class);

    @Autowired
    private ITablePictureServiceDao tablePictureServiceDaoImpl;

//    @Autowired
//    private FaceEngine faceEngine;

    @Transactional
    @Override
    public String saveTablePicture(@RequestBody TablePictureDto tablePictureDto) {
        // 是否是人脸识别照片
        Boolean isFace = tablePictureDto.getFace();
        JSONArray pictures = tablePictureDto.getPictures();
        for (int i = 0; i < pictures.size(); i++) {
            String imageBase64Picture = pictures.getJSONObject(i).getString("photo");
            if (imageBase64Picture.contains("data:image/png;base64,")) {
                imageBase64Picture = imageBase64Picture.replace("data:image/png;base64,", "");
            } else if (imageBase64Picture.contains("data:image/jpeg;base64,")) {
                imageBase64Picture = imageBase64Picture.replace("data:image/jpeg;base64,", "");
            } else if (imageBase64Picture.contains("data:image/webp;base64,")) {
                imageBase64Picture = imageBase64Picture.replace("data:image/webp;base64,", "");
            } else if (imageBase64Picture.contains("data:application/octet-stream;base64,")) {
                imageBase64Picture = imageBase64Picture.replace("data:application/octet-stream;base64,", "");
            } else if (imageBase64Picture.contains("mp4") || imageBase64Picture.contains("MP4") || imageBase64Picture.contains("AVI") || imageBase64Picture.contains("avi")
                    || imageBase64Picture.contains("WMV") || imageBase64Picture.contains("wmv")) {
                return "不支持视频上传";
            } else {
                return "未识别到图片格式";
            }

            try {
                byte[] context = Base64Convert.base64ToByte(imageBase64Picture);
                byte[] featureData = null;
//                if (Objects.nonNull(isFace) && isFace){
//                    // 需要对人脸照片进行特征码提取
//                    ImageInfo imageInfo = ImageFactory.getRGBData(context);
//
//                    // 人脸检测得到人脸列表
//                    List<FaceInfo> faceInfoList = new ArrayList<>();
//                    int detectErrorCode = faceEngine.detectFaces(imageInfo.getImageData(),
//                            imageInfo.getWidth(),
//                            imageInfo.getHeight(),
//                            imageInfo.getImageFormat(),
//                            faceInfoList);
//                    if (detectErrorCode != 0){
//                        return "人脸检测失败,errCode:" + detectErrorCode;
//                    }
//                    if (CollectionUtils.isNotEmpty(faceInfoList)){
//                        FaceFeature faceFeature = new FaceFeature();
//                        int featureErrorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(),
//                                imageInfo.getWidth(),
//                                imageInfo.getHeight(),
//                                imageInfo.getImageFormat(),
//                                faceInfoList.get(0),
//                                faceFeature);
//                        if (featureErrorCode != 0){
//                            logger.error("获取人脸特征码失败");
//                            return "获取人脸特征码失败,errCode:" + featureErrorCode;
//                        }
//                        logger.error("获取到的人脸特征码为：{}", featureErrorCode);
//                        // 人脸特征值大小
//                        featureData = faceFeature.getFeatureData();
//                    }
//                    if (Objects.isNull(featureData)){
//                        return "根据图像未获取到人脸识别码";
//                    }
//                }

                TablePictureDto toDbTablePicture = new TablePictureDto();
                toDbTablePicture.setPicture(context);
                toDbTablePicture.setPriKey(tablePictureDto.getPriKey());
                toDbTablePicture.setTableName(tablePictureDto.getTableName());
                toDbTablePicture.setPictureType(tablePictureDto.getPictureType());
                toDbTablePicture.setFeatureCode(featureData);
                if (isFace){
                    // 如果是人脸识别照片，判断照片是否已经在数据库中存在，已经存在，执行更新操作
                    TablePictureDto existPicture = new TablePictureDto();
                    existPicture.setTableName(tablePictureDto.getTableName());
                    existPicture.setPriKey(tablePictureDto.getPriKey());
                    List<Integer> exist = tablePictureServiceDaoImpl.existTablePicture(existPicture);
                    if (Objects.nonNull(exist) && exist.size() > 1){
                        // 如果能走到这里，那真是太神奇了
                        return "数据异常，检测库中存在多张相同人员人脸照片";
                    }
                    if (Objects.nonNull(exist) && !exist.isEmpty()){
                        // 存在图片，执行更新操作
                        Integer oldTablePictureId = exist.get(0);
                        toDbTablePicture.setId(oldTablePictureId);
                        int update = tablePictureServiceDaoImpl.updateTablePicture(toDbTablePicture);
                        if (update > 0){
                            return "ok";
                        }
                        return "更新失败";
                    }
                }
                int response = tablePictureServiceDaoImpl.saveTablePicture(toDbTablePicture);
                if (response < 1){
                    return "上传文件失败";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "上传文件失败";
            }
        }

        return "ok";
    }

    @Override
    public List<TablePictureDto> getPictures(@RequestBody TablePictureDto tablePictureDto) {
        return tablePictureServiceDaoImpl.getPictures(tablePictureDto);
    }

    @Override
    public TablePictureDto getPicture(Integer id) {
        return tablePictureServiceDaoImpl.getPicture(id);
    }

    @Override
    public TablePictureDto getPictureByPriKey(String priKey) {
        return tablePictureServiceDaoImpl.getPictureByPriKey(priKey);
    }
}

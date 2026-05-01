package com.java110.front.smo.ftp;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加沃文件接口
 *
 * add by wuxw 2019-06-30
 */
public interface FtpFileSMO {

    /**
     * 获取图片文件
     * @param fileId 文件id
     * @param request
     * @param response
     * @return
     */
    void getFtpImageFile(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 添加文件
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveFile(IPageData pd, MultipartFile uploadFile) throws IOException;

    /**
     * 获取文件
     * @param fileId
     * @param request
     * @param response
     * @return
     */
    void getFtpFile(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 获取pdf流
     * @param fileId
     * @param request
     * @param response
     */
    void getPdf(String fileId, HttpServletRequest request, HttpServletResponse response);
}

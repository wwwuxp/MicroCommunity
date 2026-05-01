package com.java110.front.components.ftp;

import com.java110.core.context.IPageData;
import com.java110.front.smo.ftp.FtpFileSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lmf
 * @date 2022/7/27
 */
@Component("uploadFTP")
public class UploadFtpFileComponent {

    @Autowired
    FtpFileSMO ftpFileSMO;

    /**
     * 获取word转的pdf
     * @param fileId
     * @param request
     * @param response
     * @throws IOException
     */
    public void getPdf(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException{
        ftpFileSMO.getPdf(fileId, request, response);
    }

    /**
     * 获取图片文件
     * @param fileId
     * @param request
     * @param response
     * @throws IOException
     */
    public void getImage(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException{
        ftpFileSMO.getFtpImageFile(fileId, request, response);
    }

    /**
     * 上传文件
     * @param pd 分页参数
     * @param uploadFile 附件
     * @return 返回文件信息
     */
    public ResponseEntity<String> save(IPageData pd, MultipartFile uploadFile) throws IOException {
        return ftpFileSMO.saveFile(pd, uploadFile);
    }

    /**
     * 添加或修改维修申请
     * @param fileId 文件id
     * @param request 请求体
     * @return 返回文件流
     */
    public void getFtpFile(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ftpFileSMO.getFtpFile(fileId, request, response);
    }

}

package com.java110.front.smo.ftp.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.community.FtpFile;
import com.java110.front.smo.ftp.FtpFileSMO;
import com.java110.intf.common.ITablePictureInnerServiceSMO;
import com.java110.intf.community.FtpFileDaoSMO;
import com.java110.utils.excel.Excel2PdfUtil;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.FtpFileUtil;
import com.java110.utils.word.Doc2PdfUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 查询notice服务类
 */
@Service("ftpFileSMOImpl")
public class FtpFileSMOImpl extends BaseComponentSMO implements FtpFileSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FtpFileDaoSMO ftpFileDaoSMO;

    @Autowired
    private FtpFileUtil ftpFileUtil;

    @Autowired
    ITablePictureInnerServiceSMO tablePictureInnerServiceSMO;

    @Override
    public void getFtpImageFile(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        FtpFile ftpFile = ftpFileDaoSMO.queryById(fileId);
        if (ftpFile == null) {
            throw new SMOException(HttpStatus.BAD_REQUEST.value(), "未查询到文件");
        }
        String name = ftpFile.getName();
        String fileType = ftpFile.getFileType();
        String fileName = ftpFile.getFileName();
        String filePath = ftpFile.getFilePath();
        boolean flag = fileType.equalsIgnoreCase("jpeg") ||
                fileType.equalsIgnoreCase("jpg") ||
                fileType.equalsIgnoreCase("webp") ||
                fileType.equalsIgnoreCase("bmp") ||
                fileType.equalsIgnoreCase("png");
        if (!flag) {
            throw new SMOException("文件id错误 不是图片 ");
        }
        response.reset();
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            if (ftpFile.getFileType().equalsIgnoreCase("jpeg") || ftpFile.getFileType().equalsIgnoreCase("jpg")) {
                response.setContentType("image/jpeg");
                response.setCharacterEncoding("gbk");
            } else if (ftpFile.getFileType().equalsIgnoreCase("png")) {
                response.setContentType("image/png");
                response.setCharacterEncoding("gbk");
            } else if (ftpFile.getFileType().equalsIgnoreCase("webp")) {
                response.setContentType("image/webp");
                response.setCharacterEncoding("gbk");
            } else {
                response.setContentType("text/html;charset=UTF-8");
                response.setLocale(new java.util.Locale("zh", "CN"));
                response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            }
            inputStream = ftpFileUtil.downloadFileFromFTP(name, filePath);
            outputStream = response.getOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ResponseEntity<String> saveFile(IPageData pd, MultipartFile uploadFile) throws IOException {
        // List<JSONObject> test = test(uploadFile);
        //这里是新增数据
        //获取文件名，文件大小，后缀
        String fileName = uploadFile.getOriginalFilename();
        long size = uploadFile.getSize();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //获取年月日做文件夹路径
        String dateStr = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_H).format(LocalDate.now());
        //上传文件获得修改后的文件名
        String newFileName = null;
        try {
            newFileName = ftpFileUtil.uploadFileFromFTP(uploadFile, dateStr);
        } catch (Exception e) {
            return new ResponseEntity<String>("文件上传失败", HttpStatus.EXPECTATION_FAILED);
        }
        //newFileName为空表示文件没有上传上去
        if (StringUtils.isEmpty(newFileName)) {
            return new ResponseEntity<String>("文件未上传", HttpStatus.EXPECTATION_FAILED);
        }
        //拼装数据，然后新增到数据库，返回需要的数据
        FtpFile ftpFile = new FtpFile();
        ftpFile.setFileType(suffix);
        ftpFile.setFileSize(size);
        ftpFile.setFileName(fileName);
        ftpFile.setName(newFileName);
        ftpFile.setFilePath(dateStr);
        ftpFile.setFtpId(GenerateCodeFactory.getBId());
        ftpFileDaoSMO.insert(ftpFile);

        return new ResponseEntity<String>(JSONObject.toJSONString(ftpFile), HttpStatus.OK);

    }

    @Override
    public void getFtpFile(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        FtpFile ftpFile = ftpFileDaoSMO.queryById(fileId);
        String name = ftpFile.getName();
        String fileName = ftpFile.getFileName();
        String filePath = ftpFile.getFilePath();
        response.reset();
        InputStream inputStream = null;
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setLocale(new java.util.Locale("zh", "CN"));
            response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(fileName, "UTF-8"));

            inputStream = ftpFileUtil.downloadFileFromFTP(name, filePath);
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public void getPdf(String fileId, HttpServletRequest request, HttpServletResponse response) {
        FtpFile ftpFile = ftpFileDaoSMO.queryById(fileId);
        String fileType = ftpFile.getFileType();
        //这里只要ftp文件，其他的不管
        List<String> fileTypes1 = Arrays.asList("doc", "docx");
        List<String> fileTypes2 = Arrays.asList("xls", "xlsx");
        List<String> fileTypes3 = Arrays.asList("pdf");
        if(!fileTypes1.contains(fileType) && !fileTypes2.contains(fileType) && !fileTypes3.contains(fileType)){
            response.setStatus(500);
            return;
        }
        String name = ftpFile.getName();
        String fileName = ftpFile.getFileName();
        String filePath = ftpFile.getFilePath();
        response.reset();
        InputStream inputStream = null;
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setLocale(new java.util.Locale("zh", "CN"));
            response.setHeader("Content-Disposition", "attachment; fileName=" + URLEncoder.encode(fileName, "UTF-8"));

            ServletOutputStream outputStream = response.getOutputStream();
            inputStream = ftpFileUtil.downloadFileFromFTP(name, filePath);
            if(fileTypes3.contains(fileType)){
                FileCopyUtils.copy(inputStream, outputStream);
            }
            if(fileTypes2.contains(fileType)){
                Excel2PdfUtil.excel2Pdf(inputStream, outputStream);
            }
            if(fileTypes1.contains(fileType)){
                Doc2PdfUtil.doc2Pdf(inputStream, outputStream);
            }
            // FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception e){
            }
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}

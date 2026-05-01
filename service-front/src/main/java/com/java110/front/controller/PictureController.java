package com.java110.front.controller;

import com.java110.dto.tablePicture.TablePictureDto;
import com.java110.intf.common.ITablePictureInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author hh
 * @date 2022/1/13
 */
@RestController
@RequestMapping("/callComponent/picture")
public class PictureController {

    @Autowired
    private ITablePictureInnerServiceSMO tablePictureInnerServiceSMOImpl;

    /**
     * 获取图片1
     * @param id 图片id
     * @param response 响应
     */
    @GetMapping("getPicture")
    public void getPicture(@RequestParam("id") Integer id,
                           HttpServletResponse response){
        ServletOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            TablePictureDto tablePictureDto = tablePictureInnerServiceSMOImpl.getPicture(id);

            byte[] picture = tablePictureDto.getPicture();
            response.setContentType("image/jpeg");
            response.setCharacterEncoding("UTF-8");
            inputStream = new ByteArrayInputStream(picture);
            outputStream = response.getOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取图片2
     * @param priKey 主键
     * @param response 响应
     */
    @GetMapping("getPictureByPriKey")
    public void getPictureByPriKey(@RequestParam("priKey") String priKey,
                           HttpServletResponse response){
        ServletOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        try {
            TablePictureDto tablePictureDto = tablePictureInnerServiceSMOImpl.getPictureByPriKey(priKey);

            byte[] picture = tablePictureDto.getPicture();
            response.setContentType("image/jpeg");
            response.setCharacterEncoding("UTF-8");
            inputStream = new ByteArrayInputStream(picture);
            outputStream = response.getOutputStream();
            int len;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

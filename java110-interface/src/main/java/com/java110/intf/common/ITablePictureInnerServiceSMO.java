package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.tablePicture.TablePictureDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 图片信息入库
 * @author hh
 * @date 2022/1/12
 */
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/tablePictureApi")
public interface ITablePictureInnerServiceSMO {

    /**
     * 保存表的图片
     * @param tablePictureDto 参数
     * @return string
     */
    @RequestMapping(value = "/saveTablePicture", method = RequestMethod.POST)
    String saveTablePicture(@RequestBody TablePictureDto tablePictureDto);

    /**
     * 获取图片信息
     * @param tablePictureDto 参数
     * @return List
     */
    @RequestMapping(value = "/getPictures", method = RequestMethod.POST)
    List<TablePictureDto> getPictures(@RequestBody TablePictureDto tablePictureDto);

    /**
     * 显示图片
     * @param id 图片id
     * @return TablePictureDto
     */
    @RequestMapping(value = "/getPicture", method = RequestMethod.GET)
    TablePictureDto getPicture(@RequestParam("id") Integer id);

    /**
     * 显示图片
     * @param priKey 主键
     * @return TablePictureDto
     */
    @RequestMapping(value = "/getPictureByPriKey", method = RequestMethod.GET)
    TablePictureDto getPictureByPriKey(@RequestParam("priKey") String priKey);

}

package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.attendance.AttendanceNotWorkDateDto;
import com.java110.po.attendance.AttendanceNotWorkDatePo;
import com.java110.vo.api.attendance.AttendanceNotWorkDateVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author lmf
 * @date 2022/6/28
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/attendanceNotWorkDateApi")
public interface IAttendanceNotWorkDateSMO {

    /**
     * 查询该日期是否为休息日
     * @param dateStr 日期字符串 格式为 yyyy-MM-dd
     * @return 大于0就表示存在
     */
    @RequestMapping(value = "/whetherTheDateIsARestDay", method = RequestMethod.POST)
    int whetherTheDateIsARestDay(@RequestBody String dateStr);

    /**
     * 查询非工作日列表
     * @param param 请求参数
     * @return List
     */
    @RequestMapping(value = "/attendanceNotWorkDatePageList", method = RequestMethod.POST)
    List<AttendanceNotWorkDateVo> attendanceNotWorkDatePageList(@RequestBody AttendanceNotWorkDateDto param);

    /**
     * 查询非工作日列表
     * @param param 请求参数
     * @return List
     */
    @RequestMapping(value = "/attendanceNotWorkDateCount", method = RequestMethod.POST)
    int attendanceNotWorkDateCount(@RequestBody AttendanceNotWorkDateDto param);

    /**
     * 新增、修改接口 有id为修改，无id为新增
     * @param param
     * @return
     */
    @RequestMapping(value = "/attendanceNotWorkDateSaveAndEdit", method = RequestMethod.POST)
    Integer attendanceNotWorkDateSaveAndEdit(@RequestBody AttendanceNotWorkDatePo param);

    /**
     * 删除接口
     * @param ids 数据id字符串，多个使用逗号分开
     * @return
     */
    @RequestMapping(value = "/attendanceNotWorkDateDel", method = RequestMethod.POST)
    Integer attendanceNotWorkDateDel(@RequestBody String ids);


}

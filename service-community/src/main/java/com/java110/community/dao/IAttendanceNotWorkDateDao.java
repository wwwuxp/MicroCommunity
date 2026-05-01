package com.java110.community.dao;

import com.java110.dto.attendance.AttendanceNotWorkDateDto;
import com.java110.po.attendance.AttendanceNotWorkDatePo;
import com.java110.vo.api.attendance.AttendanceNotWorkDateVo;

import java.util.List;

/**
 * @author lmf
 * 非工作日数据库查询
 * @date 2022/6/28
 */
public interface IAttendanceNotWorkDateDao {

    /**
     * 查询某日期是否为休息日
     * @param dateStr
     * @return
     */
    int whetherTheDateIsARestDay(String dateStr);

    /**
     * 分页查询数据
     * @param param 参数对象
     * @return 返回数据
     */
    List<AttendanceNotWorkDateVo> queryPageList(AttendanceNotWorkDateDto param);

    /**
     * 查询总数
     * @param param
     * @return
     */
    int queryCount(AttendanceNotWorkDateDto param);

    /**
     * 新增
     * @return
     */
    Integer save(AttendanceNotWorkDatePo param);


    /**
     * 修改
     * @return
     */
    Integer update(AttendanceNotWorkDatePo param);
}

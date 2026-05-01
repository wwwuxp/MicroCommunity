package com.java110.community.dao.impl;

import com.java110.community.dao.IAttendanceNotWorkDateDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.attendance.AttendanceNotWorkDateDto;
import com.java110.po.attendance.AttendanceNotWorkDatePo;
import com.java110.vo.api.attendance.AttendanceNotWorkDateVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lmf
 * @date 2022/6/28
 */
@Service("attendanceNotWorkDateDaoImpl")
public class AttendanceNotWorkDateDaoImpl extends BaseServiceDao implements IAttendanceNotWorkDateDao {

    @Override
    public List<AttendanceNotWorkDateVo> queryPageList(AttendanceNotWorkDateDto param) {
        return sqlSessionTemplate.selectList("attendanceNotWorkDateDaoImpl.queryPageList", param);
    }

    @Override
    public int queryCount(AttendanceNotWorkDateDto param) {
        return sqlSessionTemplate.selectOne("attendanceNotWorkDateDaoImpl.queryCount", param);
    }

    @Override
    public Integer save(AttendanceNotWorkDatePo param) {
        return sqlSessionTemplate.insert("attendanceNotWorkDateDaoImpl.saveAttendanceNotWorkDate", param);
    }

    @Override
    public Integer update(AttendanceNotWorkDatePo param) {
        return sqlSessionTemplate.update("attendanceNotWorkDateDaoImpl.updateAttendanceNotWorkDate", param);
    }

    @Override
    public int whetherTheDateIsARestDay(String dateStr) {
        return sqlSessionTemplate.selectOne("attendanceNotWorkDateDaoImpl.whetherTheDateIsARestDay", dateStr);
    }
}

package com.java110.community.dao.impl;

import com.java110.community.dao.IAttendanceExceptionServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.attendance.AttendanceExceptionDto;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.vo.api.attendanceException.ApiAttendanceExceptionDataVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hh
 * @date 2021/9/27
 */
@Service("attendanceExceptionServiceDaoImpl")
public class AttendanceExceptionServiceDaoImpl extends BaseServiceDao implements IAttendanceExceptionServiceDao {

    @Override
    public void upsertAttendanceExceptionBatch(Set<AttendanceExceptionPo> attendanceExceptionPos) {
        sqlSessionTemplate.insert("attendanceExceptionServiceDaoImpl.upsertAttendanceExceptionBatch", attendanceExceptionPos);
    }

    @Override
    public String getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth(Map<String, Object> map) {
        return sqlSessionTemplate.selectOne("attendanceExceptionServiceDaoImpl.getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth", map);
    }

    @Override
    public void insertAttendanceExceptionDataBatch(List<AttendanceExceptionDataPo> attendanceExceptionDataPos) {
        sqlSessionTemplate.insert("attendanceExceptionServiceDaoImpl.insertAttendanceExceptionDataBatch", attendanceExceptionDataPos);
    }

    @Override
    public int queryAttendanceExceptionsCount(AttendanceExceptionDto attendanceExceptionDto) {
        return sqlSessionTemplate.selectOne("attendanceExceptionServiceDaoImpl.queryAttendanceExceptionsCount", attendanceExceptionDto);
    }

    @Override
    public List<ApiAttendanceExceptionDataVo> listAttendanceExceptions(AttendanceExceptionDto attendanceExceptionDto) {
        return sqlSessionTemplate.selectList("attendanceExceptionServiceDaoImpl.listAttendanceExceptions", attendanceExceptionDto);
    }

    @Override
    public boolean verifyAttendanceException(Map<String, Object> map) {
        int update = sqlSessionTemplate.update("attendanceExceptionServiceDaoImpl.verifyAttendanceException", map);
        return update > 0;
    }

    @Override
    public boolean modifyAttendanceExceptionDataType(Map<String, Object> map) {
        int update = sqlSessionTemplate.update("attendanceExceptionServiceDaoImpl.modifyAttendanceExceptionDataType", map);
        return update > 0;
    }

    @Override
    public List<AttendanceExceptionDataPo> getAttendanceExceptionInfo(Map<String, Object> map) {
        return sqlSessionTemplate.selectList("attendanceExceptionServiceDaoImpl.getAttendanceExceptionInfo", map);
    }

    @Override
    public List<AttendanceExceptionDataPo> getAttendanceExceptionInfoByDateStr(List<String> dateList) {
        return sqlSessionTemplate.selectList("attendanceExceptionServiceDaoImpl.getAttendanceExceptionInfoByDateStr", dateList);
    }

    @Override
    public int deleteAttendanceExceptionByIds(List<String> idList) {
        return sqlSessionTemplate.delete("attendanceExceptionServiceDaoImpl.deleteAttendanceExceptionByIds", idList);
    }

}

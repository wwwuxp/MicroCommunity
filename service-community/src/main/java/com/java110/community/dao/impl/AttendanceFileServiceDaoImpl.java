package com.java110.community.dao.impl;

import com.java110.community.dao.IAttendanceFileServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.attendanceFile.ApiAttendanceFileDataVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hh
 * create date 2021/9/24
 */
@Service("attendanceFileServiceDaoImpl")
public class AttendanceFileServiceDaoImpl extends BaseServiceDao implements IAttendanceFileServiceDao {

    @Override
    public int queryAttendanceFilesCount(AttendanceFileDto attendanceFileDto) {
        return sqlSessionTemplate.selectOne("attendanceFileServiceDaoImpl.queryAttendanceFilesCount", attendanceFileDto);
    }

    @Override
    public List<ApiAttendanceFileDataVo> listAttendanceFiles(AttendanceFileDto attendanceFileDto) {
        return sqlSessionTemplate.selectList("attendanceFileServiceDaoImpl.listAttendanceFiles", attendanceFileDto);
    }

    @Override
    public boolean deleteAttendanceFileById(Integer fileId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("id", fileId);

        int update = sqlSessionTemplate.update("attendanceFileServiceDaoImpl.deleteAttendanceFileById", map);
        if (update < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "删除失败");
        }

        return true;
    }

    @Override
    public boolean deleteAttendanceFileDataByFileId(Integer fileId, String tableName) {
        Map<String, Object> map = new HashMap<>();
        map.put("fileId", fileId);
        map.put("tableName", tableName);

        int update = sqlSessionTemplate.update("attendanceFileServiceDaoImpl.deleteAttendanceFileDataByFileId", map);

        return update > 0;
    }

    @Override
    public List<AttendanceFileDto> listVacationOrRestTimesByUserInfo(UserInfoDto userInfoDto) {
        return sqlSessionTemplate.selectList("attendanceFileServiceDaoImpl.listVacationOrRestTimesByUserInfo", userInfoDto);
    }


    @Override
    public List<AttendanceFileDto> listVacationAndRestTimesByUserInfo(UserInfoDto userInfoDto) {
        return sqlSessionTemplate.selectList("attendanceFileServiceDaoImpl.listVacationAndRestTimesByUserInfo", userInfoDto);
    }

    @Override
    public Integer isVacationOrRest(UserInfoDto userInfoDto) {
        return sqlSessionTemplate.selectOne("attendanceFileServiceDaoImpl.isVacationOrRest", userInfoDto);
    }
}

package com.java110.community.smo.impl;

import com.java110.community.dao.IAttendanceFileServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.AttendanceFileDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.intf.community.IAttendanceFileInnerServiceSMO;
import com.java110.po.attendanceFile.AttendanceFilePo;
import com.java110.utils.exception.SMOException;
import com.java110.vo.api.attendanceFile.ApiAttendanceFileDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author hh
 * create date 2021/9/24
 */
@RestController
public class AttendanceFileInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceFileInnerServiceSMO {

    @Autowired
    private IAttendanceFileServiceDao attendanceFileServiceDaoImpl;

    @Override
    public int queryAttendanceFilesCount(@RequestBody AttendanceFileDto attendanceFileDto) {
        return attendanceFileServiceDaoImpl.queryAttendanceFilesCount(attendanceFileDto);
    }

    @Override
    public List<ApiAttendanceFileDataVo> queryAttendanceFiles(@RequestBody AttendanceFileDto attendanceFileDto) {
        //校验是否传了分页信息
        int page = attendanceFileDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceFileDto.setPage((page - 1) * attendanceFileDto.getRow());
        }
        return attendanceFileServiceDaoImpl.listAttendanceFiles(attendanceFileDto);
    }

    @Override
    public List<AttendanceFileDto> listVacationOrRestTimesByUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return attendanceFileServiceDaoImpl.listVacationOrRestTimesByUserInfo(userInfoDto);
    }

    @Override
    public List<AttendanceFileDto> listVacationAndRestTimesByUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return attendanceFileServiceDaoImpl.listVacationAndRestTimesByUserInfo(userInfoDto);
    }


    @Override
    public boolean isVacationOrRest(@RequestBody UserInfoDto userInfoDto) {
        Integer exist = attendanceFileServiceDaoImpl.isVacationOrRest(userInfoDto);
        return Objects.nonNull(exist);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAttendanceFileById(Integer fileId, Integer dataType) {
        if (Objects.isNull(fileId)){
            throw new SMOException("远程调用过程文件id为空了");
        }

        // 删除文件数据
        switch (dataType){
            case AttendanceFilePo.DATATYPE_VACATION:
                attendanceFileServiceDaoImpl.deleteAttendanceFileDataByFileId(fileId, AttendanceFilePo.TABLE_VACATION);
                break;
            case AttendanceFilePo.DATATYPE_REST:
                attendanceFileServiceDaoImpl.deleteAttendanceFileDataByFileId(fileId, AttendanceFilePo.TABLE_REST);
                break;
            case AttendanceFilePo.DATATYPE_OVERTIME:
                attendanceFileServiceDaoImpl.deleteAttendanceFileDataByFileId(fileId, AttendanceFilePo.TABLE_OVERTIME);
                break;
            default:
        }

        // 删除文件基本信息
        return attendanceFileServiceDaoImpl.deleteAttendanceFileById(fileId);
    }

}

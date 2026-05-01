package com.java110.community.smo.impl;

import com.java110.community.dao.IAttendanceExceptionServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.AttendanceExceptionDto;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.po.attendanceException.AttendanceExceptionDataPo;
import com.java110.po.attendanceException.AttendanceExceptionPo;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendanceException.ApiAttendanceExceptionDataVo;
import com.java110.vo.api.attendanceException.AttendanceExceptionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author hh
 * @date 2021/9/27
 */
@RestController
public class AttendanceExceptionInnerServiceSMOImpl extends BaseServiceSMO implements IAttendanceExceptionInnerServiceSMO {

    @Autowired
    private IAttendanceExceptionServiceDao attendanceExceptionServiceDaoImpl;

    @Override
    public void upsertAttendanceExceptionBatch(@RequestBody Set<AttendanceExceptionPo> attendanceExceptionPos) {
        attendanceExceptionServiceDaoImpl.upsertAttendanceExceptionBatch(attendanceExceptionPos);
    }

    @Override
    public String getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth(@RequestParam("name") String name,
                                                                                  @RequestParam("department") String department,
                                                                                  @RequestParam(value = "position", required = false) String position,
                                                                                  @RequestParam("attendanceMonth") String attendanceMonth) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("department", department);
        map.put("position", position);
        map.put("attendanceMonth", attendanceMonth);
        return attendanceExceptionServiceDaoImpl.getExceptionIdByNameAndDepartmentAndPositionAndAttendanceMonth(map);
    }

    @Override
    public void insertAttendanceExceptionDataBatch(@RequestBody List<AttendanceExceptionDataPo> attendanceExceptionDataPos) {
        attendanceExceptionServiceDaoImpl.insertAttendanceExceptionDataBatch(attendanceExceptionDataPos);
    }

    @Override
    public int queryAttendanceExceptionsCount(@RequestBody AttendanceExceptionDto attendanceExceptionDto) {
        return attendanceExceptionServiceDaoImpl.queryAttendanceExceptionsCount(attendanceExceptionDto);
    }

    @Override
    public List<ApiAttendanceExceptionDataVo> queryAttendanceExceptions(@RequestBody AttendanceExceptionDto attendanceExceptionDto) {
        //校验是否传了 分页信息
        int page = attendanceExceptionDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            attendanceExceptionDto.setPage((page - 1) * attendanceExceptionDto.getRow());
        }
        List<ApiAttendanceExceptionDataVo> apiAttendanceExceptionDataVos = attendanceExceptionServiceDaoImpl.listAttendanceExceptions(attendanceExceptionDto);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
        for (ApiAttendanceExceptionDataVo apiAttendanceExceptionDataVo : apiAttendanceExceptionDataVos) {
            List<AttendanceExceptionDetail> exceptionDatas = apiAttendanceExceptionDataVo.getExceptionData();
            for (AttendanceExceptionDetail exceptionData : exceptionDatas) {
                String attendanceDateStr = exceptionData.getAttendanceDate();
                LocalDate attendanceDate = LocalDate.parse(attendanceDateStr, dateFormatter);
                int week = attendanceDate.getDayOfWeek().getValue();
                switch (week){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5: exceptionData.setIsWorkingDay(1);break;
                    case 6:
                    case 7: exceptionData.setIsWorkingDay(0);break;
                    default:
                }
            }
        }
        return apiAttendanceExceptionDataVos;
    }

    @Override
    public boolean verifyAttendanceException(Integer id, int verifyCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("verifyCode", verifyCode);
        return attendanceExceptionServiceDaoImpl.verifyAttendanceException(map);
    }

    @Override
    public boolean modifyAttendanceExceptionDataType(String exceptionDataId, Integer type) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", exceptionDataId);
        map.put("type", type);
        return attendanceExceptionServiceDaoImpl.modifyAttendanceExceptionDataType(map);
    }

    @Override
    public List<AttendanceExceptionDataPo> getAttendanceExceptionInfo(String attendanceMonth) {
        Map<String, Object> map = new HashMap<>();
        map.put("attendanceMonth", attendanceMonth);
        return attendanceExceptionServiceDaoImpl.getAttendanceExceptionInfo(map);
    }

    @Override
    public List<AttendanceExceptionDataPo> getAttendanceExceptionInfoByDateStr(String dateStr) {
        return attendanceExceptionServiceDaoImpl.getAttendanceExceptionInfoByDateStr(Arrays.asList(dateStr.split(",")));
    }

    @Override
    public int deleteAttendanceExceptionByIds(@RequestBody List<String> ids){
        return attendanceExceptionServiceDaoImpl.deleteAttendanceExceptionByIds(ids);
    }
}

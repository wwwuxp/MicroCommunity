package com.java110.community.smo.impl;

import com.java110.community.dao.IAttendanceNotWorkDateDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.AttendanceNotWorkDateDto;
import com.java110.intf.community.IAttendanceNotWorkDateSMO;
import com.java110.po.attendance.AttendanceNotWorkDatePo;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendance.AttendanceNotWorkDateVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lmf
 * @date 2022/6/28
 */
@RestController
public class AttendanceNotWorkDateSMOImpl extends BaseServiceSMO implements IAttendanceNotWorkDateSMO {

    @Autowired
    IAttendanceNotWorkDateDao attendanceNotWorkDateDao;


    @Override
    public int whetherTheDateIsARestDay(@RequestBody String dateStr) {
        if(DateUtil.judgeDate(dateStr, "yyyy-MM-dd")){
            return attendanceNotWorkDateDao.whetherTheDateIsARestDay(dateStr);
        }else{
            return 0;
        }
    }

    @Override
    public List<AttendanceNotWorkDateVo> attendanceNotWorkDatePageList(@RequestBody AttendanceNotWorkDateDto param) {
        int page = param.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            param.setDataPage();
        }
        return attendanceNotWorkDateDao.queryPageList(param);
    }

    @Override
    public int attendanceNotWorkDateCount(@RequestBody AttendanceNotWorkDateDto param) {
        return attendanceNotWorkDateDao.queryCount(param);
    }

    @Override
    public Integer attendanceNotWorkDateSaveAndEdit(@RequestBody AttendanceNotWorkDatePo param) {
        String id = param.getId();
        Integer result = 0;
        if(StringUtils.isNotEmpty(id)){
            result = attendanceNotWorkDateDao.update(param);
        }else {
            result = attendanceNotWorkDateDao.save(param);
        }
        return result;
    }

    @Override
    public Integer attendanceNotWorkDateDel(@RequestBody String ids) {
        Integer result = 0;
        if(StringUtils.isNotEmpty(ids)){
            String[] idArr = ids.split(",");
            for (int i = 0; i < idArr.length; i++) {
                AttendanceNotWorkDatePo po = new AttendanceNotWorkDatePo();
                po.setId(idArr[i]);
                po.setDeletedFlag("1");
                result += attendanceNotWorkDateDao.update(po);
            }
        }
        return result;
    }


}

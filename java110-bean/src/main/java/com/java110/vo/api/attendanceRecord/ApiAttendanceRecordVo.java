package com.java110.vo.api.attendanceRecord;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/24
 */
public class ApiAttendanceRecordVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 549682351648176912L;

    private List<ApiAttendanceRecordDataVo> data;

    public List<ApiAttendanceRecordDataVo> getData() {
        return data;
    }

    public void setData(List<ApiAttendanceRecordDataVo> data) {
        this.data = data;
    }
}

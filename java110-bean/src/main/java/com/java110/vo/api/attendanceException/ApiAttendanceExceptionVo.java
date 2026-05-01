package com.java110.vo.api.attendanceException;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/10/8
 */
public class ApiAttendanceExceptionVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -4945535322941103807L;

    private List<ApiAttendanceExceptionDataVo> data;

    public List<ApiAttendanceExceptionDataVo> getData() {
        return data;
    }

    public void setData(List<ApiAttendanceExceptionDataVo> data) {
        this.data = data;
    }
}

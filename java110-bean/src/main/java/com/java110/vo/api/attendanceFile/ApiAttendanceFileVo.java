package com.java110.vo.api.attendanceFile;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/9/27
 */
public class ApiAttendanceFileVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = -5538192579673913705L;

    private List<ApiAttendanceFileDataVo> data;

    public List<ApiAttendanceFileDataVo> getData() {
        return data;
    }

    public void setData(List<ApiAttendanceFileDataVo> data) {
        this.data = data;
    }
}

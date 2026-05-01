package com.java110.vo.api.attendanceNoonRecord;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * @date 2021/12/17
 */
public class ApiAttendanceNoonRecordVo extends MorePageVo implements Serializable {

    private static final long serialVersionUID = 1395276993475504594L;

    private List<ApiAttendanceNoonRecordDataVo> data;

    public List<ApiAttendanceNoonRecordDataVo> getData() {
        return data;
    }

    public void setData(List<ApiAttendanceNoonRecordDataVo> data) {
        this.data = data;
    }
}

package com.java110.entity.attendance;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/11/24
 */
@ContentRowHeight(20)
@HeadRowHeight(25)
public class ExportAttendanceRecordOrException implements Serializable {

    private static final long serialVersionUID = 5452120432308383851L;

    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String name;

    @ExcelProperty("部门")
    @ColumnWidth(25)
    private String department;

    @ExcelProperty("考勤日期")
    @ColumnWidth(20)
    private String attendanceDate;

    @ExcelProperty("实际上班时间")
    @ColumnWidth(20)
    private String actualStartWorkTime;

    @ExcelProperty("实际下班时间")
    @ColumnWidth(20)
    private String actualEndWorkTime;

    @ExcelProperty("类型")
    @ColumnWidth(10)
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getActualStartWorkTime() {
        return actualStartWorkTime;
    }

    public void setActualStartWorkTime(String actualStartWorkTime) {
        this.actualStartWorkTime = actualStartWorkTime;
    }

    public String getActualEndWorkTime() {
        return actualEndWorkTime;
    }

    public void setActualEndWorkTime(String actualEndWorkTime) {
        this.actualEndWorkTime = actualEndWorkTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.java110.utils.constant;

/**
 * 考勤管理常量类
 * @author hh
 * create date 2021/9/23
 */
public class ServiceCodeAttendanceConstant {

    /**
     * 查询通行记录列表
     */
    public static final String LIST_PASS_RECORD = "passRecord.listPassRecords";

    /**
     * 查询考勤记录列表
     */
    public static final String LIST_ATTENDANCE_RECORD = "attendanceRecord.listAttendanceRecords";

    /**
     * 查询考勤导入文件列表
     */
    public static final String LIST_ATTENDANCE_FILE = "attendanceFile.listAttendanceFiles";

    /**
     * 查询考勤异常列表
     */
    public static final String LIST_ATTENDANCE_EXCEPTION = "attendanceException.listAttendanceExceptions";

    /**
     * 核实考勤异常
     */
    public static final String VERIFY_ATTENDANCE_EXCEPTION = "attendanceException.verifyAttendanceException";

    /**
     * 考勤异常 - 修改类型
     */
    public static final String MODIFY_ATTENDANCE_EXCEPTION_DATA = "attendanceExceptionData.modifyDataType";

    /**
     * 查询中午考勤记录列表
     */
    public static final String LIST_ATTENDANCE_NOON_RECORD = "attendanceNoonRecord.listAttendanceNoonRecords";

    /**
     * 查询非工作日列表
     */
    public static final String LIST_ATTENDANCE_NOT_WORK_DATE = "attendanceNotWorkDate.listAttendanceNotWorkDate";
    /**
     * 修改/新增非工作日
     */
    public static final String SAVE_OR_EDIT_ATTENDANCE_NOT_WORK_DATE = "attendanceNotWorkDate.saveOrEdit";

    /**
     * 删除非工作日
     */
    public static final String DELETE_ATTENDANCE_NOT_WORK_DATE = "attendanceNotWorkDate.delete";
}

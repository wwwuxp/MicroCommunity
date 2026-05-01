package com.java110.po.attendanceFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 考勤管理 - 导入文件 - 文件基本信息
 * @author hh
 * create date 2021/9/26
 */
public class AttendanceFilePo implements Serializable {

    private static final long serialVersionUID = 6901333171596843740L;

    /**
     * 个人休假数据
     */
    public static final int DATATYPE_VACATION = 1;

    /**
     * 调休数据
     */
    public static final int DATATYPE_REST = 2;

    /**
     * 加班数据
     */
    public static final int DATATYPE_OVERTIME = 3;

    /**
     * 综合休假数据
     */
    public static final int DATATYPE_COMP_VACATION = 4;

    /**
     * 休假数据表名
     */
    public static final String TABLE_VACATION = "attendance_file_vacation";

    /**
     * 调休数据表名
     */
    public static final String TABLE_REST = "attendance_file_rest";

    /**
     * 加班数据表名
     */
    public static final String TABLE_OVERTIME = "attendance_file_overtime";

    private Integer id;
    private String fileName;
    private String fileTime;
    private Integer dataType;
    private Date importTime;
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

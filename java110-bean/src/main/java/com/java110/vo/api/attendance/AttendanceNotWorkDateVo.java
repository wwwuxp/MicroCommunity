package com.java110.vo.api.attendance;

import com.java110.vo.MorePageVo;

import java.io.Serializable;

/**
 * @author lmf
 * 非工作日返回数据
 * @date 2022/6/28
 */
public class AttendanceNotWorkDateVo extends MorePageVo implements Serializable {
    private static final long serialVersionUID = -2514799229625193044L;
    /**
     * 主键
     */
    private String id;
    /**
     * 日期本体
     */
    private String content;
    /**
     * 年份
     */
    private String year;
    /**
     * 类型。1：非工作日，2：节假日，3：其它
     */
    private Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

package com.java110.entity.diningRecord;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/11/12
 */
@ContentRowHeight(20)
@HeadRowHeight(25)
public class DiningStatistics implements Serializable {

    private static final long serialVersionUID = -4220446570259230227L;

    @ExcelProperty("姓名")
    @ColumnWidth(20)
    private String name;

    @ColumnWidth(25)
    @ExcelProperty("部门")
    private String department;

    /*@ExcelProperty("职务")
    @ColumnWidth(20)
    private String position;*/

    @ExcelProperty("统计时间")
    @ColumnWidth(25)
    private String countTime;

    @ExcelProperty("上午就餐数")
    @ColumnWidth(18)
    private Integer morning;

    @ExcelProperty("中午就餐数")
    @ColumnWidth(18)
    private Integer noon;

    @ExcelProperty("晚上就餐数")
    @ColumnWidth(18)
    private Integer afternoon;

    @ExcelProperty("总就餐数")
    @ColumnWidth(18)
    private Integer total;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountTime() {
        return countTime;
    }

    public void setCountTime(String countTime) {
        this.countTime = countTime;
    }

    public Integer getMorning() {
        return morning;
    }

    public void setMorning(Integer morning) {
        this.morning = morning;
    }

    public Integer getNoon() {
        return noon;
    }

    public void setNoon(Integer noon) {
        this.noon = noon;
    }

    public Integer getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(Integer afternoon) {
        this.afternoon = afternoon;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}

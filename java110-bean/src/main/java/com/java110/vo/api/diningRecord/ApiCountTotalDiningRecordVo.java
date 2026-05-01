package com.java110.vo.api.diningRecord;

import java.io.Serializable;

/**
 * @author hh
 * @date 2021/11/26
 */
public class ApiCountTotalDiningRecordVo implements Serializable {

    private static final long serialVersionUID = -8558759151628159609L;

    /**
     * 总就餐数
     */
    private Integer total;

    /**
     * 上午就餐数
     */
    private Integer morning;

    /**
     * 中午就餐数
     */
    private Integer noon;

    /**
     * 晚上就餐数
     */
    private Integer afternoon;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
}

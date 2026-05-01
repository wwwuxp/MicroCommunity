package com.java110.vo.api.pubCarDataScreen;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/8/16 16:09
 */
public class DataScreenAnalysisData implements Serializable {

    private static final long serialVersionUID = 2970575483323429671L;

    private String name;

    private Integer count;

    private Double mileage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }
}

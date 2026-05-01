package com.java110.vo.api.keyCabinetSn;

import java.io.Serializable;

/**
 * @author hh
 * @date 2022/2/19
 */
public class ApiKeyCabinetSnDataVo implements Serializable {

    private static final long serialVersionUID = -3284613528802103554L;

    private Integer id;
    private String name;
    private Integer quantity;
    private String deviceSn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
}

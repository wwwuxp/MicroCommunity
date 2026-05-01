package com.java110.vo.api.resourceOrder;

import java.io.Serializable;

/**
 * @author hh
 * create date 2021/9/17
 */
public class ApiResourceSimpleDataVo implements Serializable {

    private static final long serialVersionUID = 6445977692263624334L;

    private String id;
    private String resName;
    private String quantity;
    private String stock;
    private String resId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }
}

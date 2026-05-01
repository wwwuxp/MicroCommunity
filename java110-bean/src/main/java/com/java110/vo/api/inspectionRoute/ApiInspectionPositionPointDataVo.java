package com.java110.vo.api.inspectionRoute;

import java.io.Serializable;

/**
 * @author hh
 * create date 2021/9/18
 */
public class ApiInspectionPositionPointDataVo implements Serializable {

    private static final long serialVersionUID = 7306082416929316992L;

    private String inspectionId;
    private String inspectionName;

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }
}

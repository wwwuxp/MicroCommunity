package com.java110.vo.api.inspectionPoint;

import java.io.Serializable;

/**
 * 识别二维码后返回的信息
 * @author hh
 * create date 2021/9/15
 */
public class InspectionPointSimpleDataVo implements Serializable {

    private static final long serialVersionUID = -7037185869606387534L;

    private String inspectionName;
    private String pointObjName;

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getPointObjName() {
        return pointObjName;
    }

    public void setPointObjName(String pointObjName) {
        this.pointObjName = pointObjName;
    }
}

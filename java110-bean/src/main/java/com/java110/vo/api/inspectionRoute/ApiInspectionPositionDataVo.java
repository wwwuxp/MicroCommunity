package com.java110.vo.api.inspectionRoute;

import java.io.Serializable;
import java.util.List;

/**
 * @author hh
 * create date 2021/9/18
 */
public class ApiInspectionPositionDataVo implements Serializable {

    private static final long serialVersionUID = 421340441324112008L;

    private String inspectionRouteId;
    private String routeName;

    private List<ApiInspectionPositionPointDataVo> positionPoints;

    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<ApiInspectionPositionPointDataVo> getPositionPoints() {
        return positionPoints;
    }

    public void setPositionPoints(List<ApiInspectionPositionPointDataVo> positionPoints) {
        this.positionPoints = positionPoints;
    }
}

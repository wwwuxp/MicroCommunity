package com.java110.vo.api.pubCarTripMileage;

import java.io.Serializable;

/**
 * @author hh
 * @datetime 2021/8/23 15:04
 */
public class ApiTripIsInProgressDataVo implements Serializable {

    private static final long serialVersionUID = 6472383222194110706L;

    private Boolean isInProgress;

    public Boolean getInProgress() {
        return isInProgress;
    }

    public void setInProgress(Boolean inProgress) {
        isInProgress = inProgress;
    }
}

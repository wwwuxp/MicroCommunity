package com.java110.vo.api.pubCarDataScreen;

import java.io.Serializable;
import java.util.List;

public class ApiPubCarDataScreenOverviewAndAnalysisVo implements Serializable {

    private static final long serialVersionUID = -8658913604531492120L;

    ApiPubCarDataScreenOverviewAndAnalysisDataVo data;

    public ApiPubCarDataScreenOverviewAndAnalysisDataVo getData() {
        return data;
    }

    public void setData(ApiPubCarDataScreenOverviewAndAnalysisDataVo data) {
        this.data = data;
    }
}

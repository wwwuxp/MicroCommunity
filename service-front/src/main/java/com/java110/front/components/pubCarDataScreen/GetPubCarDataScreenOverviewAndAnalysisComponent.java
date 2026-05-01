package com.java110.front.components.pubCarDataScreen;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarDataScreen.IGetDataScreenOverviewAndAnalysisSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 数据总览和分析部分数据 数据显示
 * @author hh
 * @datetime 2021/8/16 13:59
 */
@Component("dataScreenOverviewAndAnalysis")
public class GetPubCarDataScreenOverviewAndAnalysisComponent {

    @Autowired
    private IGetDataScreenOverviewAndAnalysisSMO getDataScreenOverviewAndAnalysisSMOImpl;

    public ResponseEntity<String> get(IPageData pd){
        return getDataScreenOverviewAndAnalysisSMOImpl.getOverviewAndAnalysis(pd);
    }

}

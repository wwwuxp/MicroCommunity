package com.java110.front.smo.pubCarDataScreen;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @datetime 2021/8/16 14:03
 */
public interface IGetDataScreenOverviewAndAnalysisSMO {

    ResponseEntity<String> getOverviewAndAnalysis(IPageData pd);

}

package com.java110.front.smo.pubCarStatistics;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 车辆统计
 * add by hh
 */
public interface IListCarStatisticsSMO {

    ResponseEntity<String> listCarStatistics(IPageData pd);

}

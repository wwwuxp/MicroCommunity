package com.java110.front.smo.pubCarStatistics;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 司机用车统计
 * add by hh
 */
public interface IListDriverStatisticsSMO {

    ResponseEntity<String> listDriverStatistics(IPageData pd);

}

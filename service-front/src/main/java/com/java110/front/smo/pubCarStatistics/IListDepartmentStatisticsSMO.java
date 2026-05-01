package com.java110.front.smo.pubCarStatistics;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 部门用车统计
 * add by hh
 */
public interface IListDepartmentStatisticsSMO {

    ResponseEntity<String> listDepartmentStatistics(IPageData pd);

}

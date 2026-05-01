package com.java110.front.smo.diningRecord;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/11/12
 */
public interface IExportDiningStatisticsSMO {

    /**
     * 就餐统计数据导出
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<Object> exportExcelData(IPageData pd);

}

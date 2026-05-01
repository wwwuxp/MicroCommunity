package com.java110.front.components.diningRecord;

import com.java110.core.context.IPageData;
import com.java110.front.smo.diningRecord.IExportDiningStatisticsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 导出就餐统计
 * @author hh
 * @date 2021/11/12
 */
@Component("exportDiningStatistics")
public class ExportDiningStatisticsComponent {

    @Autowired
    private IExportDiningStatisticsSMO exportDiningStatisticsSMOImpl;

    /**
     * 导出就餐统计
     * @param pd 前端请求参数封装
     * @return responseEntity
     * @throws Exception
     */
    public ResponseEntity<Object> exportData(IPageData pd) throws Exception {
        return exportDiningStatisticsSMOImpl.exportExcelData(pd);
    }

}

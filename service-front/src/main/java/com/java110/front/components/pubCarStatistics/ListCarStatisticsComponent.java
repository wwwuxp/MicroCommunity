package com.java110.front.components.pubCarStatistics;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarStatistics.IListCarStatisticsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 车辆统计
 * add by hh
 */
@Component("listCarStatistics")
public class ListCarStatisticsComponent {

    @Autowired
    private IListCarStatisticsSMO listCarStatisticsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listCarStatisticsSMOImpl.listCarStatistics(pd);
    }

}

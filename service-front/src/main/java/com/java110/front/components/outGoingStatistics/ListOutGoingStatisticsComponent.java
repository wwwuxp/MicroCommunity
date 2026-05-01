package com.java110.front.components.outGoingStatistics;

import com.java110.core.context.IPageData;
import com.java110.front.smo.outGoingStatistics.IListOutGoingStatisticsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2021/12/20
 */
@Component("listOutGoingStatistics")
public class ListOutGoingStatisticsComponent {

    @Autowired
    private IListOutGoingStatisticsSMO listOutGoingStatisticsSMOImpl;

    /**
     * 查询外出统计列表
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listOutGoingStatisticsSMOImpl.listOutGoingStatistics(pd);
    }

}

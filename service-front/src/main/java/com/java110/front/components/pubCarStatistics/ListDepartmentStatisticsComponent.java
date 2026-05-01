package com.java110.front.components.pubCarStatistics;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarStatistics.IListDepartmentStatisticsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 部门统计
 * add by hh
 */
@Component("listDepartmentStatistics")
public class ListDepartmentStatisticsComponent {

    @Autowired
    private IListDepartmentStatisticsSMO listPubCarDepartmentStatisticsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarDepartmentStatisticsSMOImpl.listDepartmentStatistics(pd);
    }

}

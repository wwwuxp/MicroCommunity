package com.java110.front.smo.outGoingStatistics;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/12/20
 */
public interface IListOutGoingStatisticsSMO {

    /**
     * 查询外出统计列表
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> listOutGoingStatistics(IPageData pd);

}

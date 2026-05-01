package com.java110.front.smo.diningRecord;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 就餐统计
 * @author hh
 * @date 2021/11/11
 */
public interface ICountDiningRecord {

    /**
     * 统计就餐记录
     * @param pd 前端参数封装
     * @return responseEntity
     */
    ResponseEntity<String> countDiningRecord(IPageData pd);

}

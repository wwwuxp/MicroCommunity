package com.java110.front.smo.diningRecord;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/11/26
 */
public interface ICountTotalDiningRecord {

    /**
     * 统计总的就餐数据
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> countTotalDiningRecord(IPageData pd);

}

package com.java110.front.components.diningRecord;

import com.java110.core.context.IPageData;
import com.java110.front.smo.diningRecord.ICountDiningRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 就餐统计
 * @author hh
 * @date 2021/11/11
 */
@Component("countDiningRecord")
public class CountDiningRecordComponent {

    @Autowired
    private ICountDiningRecord countDiningRecordSMOImpl;

    /**
     * 就餐
     * @param pd 请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> count(IPageData pd) {
        return countDiningRecordSMOImpl.countDiningRecord(pd);
    }

}

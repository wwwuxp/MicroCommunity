package com.java110.front.components.diningRecord;

import com.java110.core.context.IPageData;
import com.java110.front.smo.diningRecord.ICountTotalDiningRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 统计总的就餐数据
 * @author hh
 * @date 2021/11/26
 */
@Component("countTotalDiningRecord")
public class CountTotalDiningRecordComponent {

    @Autowired
    private ICountTotalDiningRecord countTotalDiningRecordSMOImpl;

    /**
     * 统计总的就餐数据
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> count(IPageData pd) {
        return countTotalDiningRecordSMOImpl.countTotalDiningRecord(pd);
    }

}

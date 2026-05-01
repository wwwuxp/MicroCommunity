package com.java110.front.smo.pubCarTrip;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/4/15
 */
public interface IUpdateTripMileageAndFeeSMO {

    /**
     * 更新行程里程和费用
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<String> updateTripMileageAndFee(IPageData pd);

}

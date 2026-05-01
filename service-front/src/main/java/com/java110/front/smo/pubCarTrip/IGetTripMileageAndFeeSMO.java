package com.java110.front.smo.pubCarTrip;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/4/15
 */
public interface IGetTripMileageAndFeeSMO {

    /**
     * 获取行程里程登记信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<String> getTripMileageAndFee(IPageData pd);

}

package com.java110.front.smo.pubCarTrip;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/4/6
 */
public interface IGetEndMileageOfPreviousCarSMO {

    /**
     * 获取先前车辆的结束里程
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<String> getEndMileageOfPreviousCar(IPageData pd);

}

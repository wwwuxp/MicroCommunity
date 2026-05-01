package com.java110.front.smo.diningRecord;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/11/12
 */
public interface IGetStaffDiningDetail {

    /**
     * 获取员工就餐详情
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> getStaffDiningDetail(IPageData pd);

}


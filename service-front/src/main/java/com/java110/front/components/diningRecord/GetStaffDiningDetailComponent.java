package com.java110.front.components.diningRecord;

import com.java110.core.context.IPageData;
import com.java110.front.smo.diningRecord.IGetStaffDiningDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 获取员工就餐详情
 * @author hh
 * @date 2021/11/12
 */
@Component("getStaffDiningDetail")
public class GetStaffDiningDetailComponent {

    @Autowired
    private IGetStaffDiningDetail getStaffDiningDetailSMOImpl;

    /**
     * 获取员工就餐详情
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> get(IPageData pd) {
        return getStaffDiningDetailSMOImpl.getStaffDiningDetail(pd);
    }

}

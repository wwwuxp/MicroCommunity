package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IGetTripMileageAndFeeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2022/4/15
 */
@Component("getTripMileageAndFee")
public class GetTripMileageAndFeeComponent {

    @Autowired
    private IGetTripMileageAndFeeSMO getTripMileageAndFeeSMOImpl;

    /**
     * 获取行程里程登记信息
     * @param pd 前端请求参数
     * @return ResponseEntity
     */
    public ResponseEntity<String> get(IPageData pd) {
        return getTripMileageAndFeeSMOImpl.getTripMileageAndFee(pd);
    }

}

package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IUpdateTripMileageAndFeeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2022/4/15
 */
@Component("updateTripMileageAndFee")
public class UpdateTripMileageAndFeeComponent {

    @Autowired
    private IUpdateTripMileageAndFeeSMO updateTripMileageAndFeeSMOImpl;

    /**
     * 更新行程里程和费用信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> update(IPageData pd) {
        return updateTripMileageAndFeeSMOImpl.updateTripMileageAndFee(pd);
    }

}

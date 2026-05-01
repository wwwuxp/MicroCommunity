package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IGetEndMileageOfPreviousCarSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 获取先前车的结束里程
 * @author hh
 * @date 2022/4/6
 */
@Component("getEndMileageOfPreviousCar")
public class GetEndMileageOfPreviousCarComponent {

    @Autowired
    private IGetEndMileageOfPreviousCarSMO getEndMileageOfPreviousCarSMOImpl;

    /**
     * 获取先前车的结束里程
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> get(IPageData pd) {
        return getEndMileageOfPreviousCarSMOImpl.getEndMileageOfPreviousCar(pd);
    }
}

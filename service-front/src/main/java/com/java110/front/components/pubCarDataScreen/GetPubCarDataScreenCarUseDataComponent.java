package com.java110.front.components.pubCarDataScreen;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarDataScreen.IGetDataScreenCarUseDataSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 当天用车数据显示
 * @author hh
 * @datetime 2021/8/17 16:56
 */
@Component("dataScreenTodayCarUseData")
public class GetPubCarDataScreenCarUseDataComponent {

    @Autowired
    private IGetDataScreenCarUseDataSMO getDataScreenCarUseDataSMOImpl;

    public ResponseEntity<String> get(IPageData pd){
        return getDataScreenCarUseDataSMOImpl.getCarUseData(pd);
    }
}

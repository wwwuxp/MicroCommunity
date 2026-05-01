package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IListPubCarTripsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2021/10/9
 */
@Component("choosePubCarTrip")
public class ChoosePubCarTripComponent {

    @Autowired
    private IListPubCarTripsSMO listPubCarTripsSMOImpl;

    /**
     * 选择行程
     * @param pd 请求数据封装
     * @return responseEntity
     */
    public ResponseEntity<String> choose(IPageData pd) {
        return listPubCarTripsSMOImpl.choosePubCarTrip(pd);
    }
}

package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IListPubCarTripsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * wen后端查询行程信息
 * add by hh
 */
@Component("listPubCarTrip")
public class ListPubCarTripComponent {

    @Autowired
    private IListPubCarTripsSMO listPubCarTripsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarTripsSMOImpl.listPubCarTrips(pd);
    }


}

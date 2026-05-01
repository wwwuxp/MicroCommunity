package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IListPubCarTripsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 行程管理表组件管理类
 * add by hh
 */
@Component("pubCarTripManage")
public class PubCarTripManageComponent {

    @Autowired
    private IListPubCarTripsSMO listPubCarTripsSMOImpl;

    /**
     * 查询行程管理表列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listPubCarTripsSMOImpl.listPubCarTrips(pd);
    }

}

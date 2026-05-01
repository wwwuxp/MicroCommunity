package com.java110.front.components.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarTrip.IDeletePubCarTripSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加行程管理表组件
 */
@Component("deletePubCarTrip")
public class DeletePubCarTripComponent {

    @Autowired
    private IDeletePubCarTripSMO deletePubCarTripSMOImpl;

    /**
     * 添加行程管理表数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePubCarTripSMOImpl.deletePubCarTrip(pd);
    }

}

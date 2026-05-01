package com.java110.front.components.pubCar;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.IDeletePubCarSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除车辆组件
 */
@Component("deletePubCar")
public class DeletePubCarComponent {

    @Autowired
    private IDeletePubCarSMO deletePubCarSMOImpl;

    /**
     * 添加车辆管理表数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePubCarSMOImpl.deletePubCar(pd);
    }

}

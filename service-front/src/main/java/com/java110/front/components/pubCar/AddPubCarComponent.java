package com.java110.front.components.pubCar;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.IAddPubCarSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加车辆组件
 */
@Component("addPubCar")
public class AddPubCarComponent {

    @Autowired
    private IAddPubCarSMO addPubCarSMOImpl;

    /**
     * 添加车辆管理表数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addPubCarSMOImpl.savePubCar(pd);
    }

}

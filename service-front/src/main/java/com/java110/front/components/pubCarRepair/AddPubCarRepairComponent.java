package com.java110.front.components.pubCarRepair;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarRepair.IAddPubCarRepairSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加车辆维修信息表组件
 * add by hh
 */
@Component("addPubCarRepair")
public class AddPubCarRepairComponent {

    @Autowired
    private IAddPubCarRepairSMO addPubCarRepairSMOImpl;

    /**
     * 添加车辆维修信息表数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addPubCarRepairSMOImpl.savePubCarRepair(pd);
    }

}

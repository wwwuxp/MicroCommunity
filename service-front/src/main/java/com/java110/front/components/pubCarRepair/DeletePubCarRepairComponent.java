package com.java110.front.components.pubCarRepair;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarRepair.IDeletePubCarRepairSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加车辆维修信息表组件
 */
@Component("deletePubCarRepair")
public class DeletePubCarRepairComponent {

    @Autowired
    private IDeletePubCarRepairSMO deletePubCarRepairSMOImpl;

    /**
     * 添加车辆维修信息表数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePubCarRepairSMOImpl.deletePubCarRepair(pd);
    }

}

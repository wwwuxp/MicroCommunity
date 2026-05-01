package com.java110.front.components.pubCar.repairForm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.repairForm.CachePubcarRepairFromSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author lmf
 * @date 2023/3/6
 */
@Component("cachePubcarRepairFrome")
public class CachePubcarRepairFromeComponet {

    @Autowired
    CachePubcarRepairFromSMO cachePubcarRepairFromSMO;

    /**
     * 添加或修改维修申请
     * @param pd 分页参数
     * @return
     */
    public ResponseEntity<String> get(IPageData pd){
        return cachePubcarRepairFromSMO.get(pd);
    }

    /**
     * 添加或修改维修申请
     * @param pd 分页参数
     * @return
     */
    public ResponseEntity<String> save(IPageData pd){
        return cachePubcarRepairFromSMO.save(pd);
    }


}

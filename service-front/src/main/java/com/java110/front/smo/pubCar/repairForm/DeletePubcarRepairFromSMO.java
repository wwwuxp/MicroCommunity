package com.java110.front.smo.pubCar.repairForm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author lmf
 * @date 2022/7/26
 */
public interface DeletePubcarRepairFromSMO {

    /**
     * 新增或者修改维修工单
     * @param pd
     * @return
     */
    ResponseEntity<String> delete(IPageData pd);

}

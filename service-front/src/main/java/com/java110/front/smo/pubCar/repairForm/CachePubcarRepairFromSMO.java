package com.java110.front.smo.pubCar.repairForm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author lmf
 * @date 2022/7/26
 */
public interface CachePubcarRepairFromSMO {

    /**
     * 添加保存
     * @param pd
     * @return
     */
    ResponseEntity<String> save(IPageData pd);

    /**
     * 获取保存
     * @param pd
     * @return
     */
    ResponseEntity<String> get(IPageData pd);

}

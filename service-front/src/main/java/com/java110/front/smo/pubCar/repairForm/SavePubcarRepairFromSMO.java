package com.java110.front.smo.pubCar.repairForm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lmf
 * @date 2022/7/26
 */
public interface SavePubcarRepairFromSMO {

    /**
     * 新增或者修改维修工单
     * @param pd
     * @param uploadFile
     * @return
     */
    ResponseEntity<String> saveOrEdit(IPageData pd, MultipartFile uploadFile);

}

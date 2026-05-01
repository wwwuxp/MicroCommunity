package com.java110.front.components.pubCar.repairForm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.repairForm.DeletePubcarRepairFromSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author lmf
 * @date 2022/7/26
 */
@Component("deletePubcarRepairFrom")
public class DeletePubcarRepairFromComponent {

    @Autowired
    DeletePubcarRepairFromSMO deletePubcarRepairFromSMO;

    /**
     * 添加或修改维修申请
     * @param pd 分页参数
     * @return
     */
    public ResponseEntity<String> delete(IPageData pd){
        return deletePubcarRepairFromSMO.delete(pd);
    }

}

package com.java110.front.components.pubCar.repairForm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.repairForm.ListPubcarRepairFromSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author lmf
 * @date 2022/7/26
 */
@Component("listPubcarRepairFromComponent")
public class ListPubcarRepairFromComponent {

    @Autowired
    ListPubcarRepairFromSMO listPubcarRepairFromSMO;

    /**
     * 添加或修改维修申请
     * @param pd 分页参数
     * @return
     */
    public ResponseEntity<String> quertyPageList(IPageData pd){
        return listPubcarRepairFromSMO.quertyPageList(pd);
    }

}

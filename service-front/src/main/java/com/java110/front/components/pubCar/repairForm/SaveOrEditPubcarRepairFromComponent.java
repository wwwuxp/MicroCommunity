package com.java110.front.components.pubCar.repairForm;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCar.repairForm.SavePubcarRepairFromSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lmf
 * @date 2022/7/26
 */
@Component("saveOrEditPubcarRepairFromComponent")
public class SaveOrEditPubcarRepairFromComponent {

    @Autowired
    SavePubcarRepairFromSMO savePubcarRepairFromSMO;

    /**
     * 添加或修改维修申请
     * @param pd 分页参数
     * @param uploadFile 附件
     * @return
     */
    public ResponseEntity<String> saveOrEdit(IPageData pd, MultipartFile uploadFile){
        return savePubcarRepairFromSMO.saveOrEdit(pd, uploadFile);
    }

    /**
     * 添加或修改维修申请
     * @param pd 分页参数
     * @return
     */
    public ResponseEntity<String> saveOrEdit(IPageData pd){
        return savePubcarRepairFromSMO.saveOrEdit(pd, null);
    }

    /**
     * 添加或修改维修申请 不带附件
     * @param pd 分页参数
     * @return
     */
    public ResponseEntity<String> approval(IPageData pd){
        return savePubcarRepairFromSMO.saveOrEdit(pd, null);
    }

}

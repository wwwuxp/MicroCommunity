package com.java110.front.components.pubCarReimburse;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarReimburse.IExportPubCarReimburseSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 用车报销 - 导出
 * @author hh
 * @date 2021/10/27
 */
@Component("exportPubCarReimburse")
public class ExportPubCarReimburseComponent {

    @Autowired
    private IExportPubCarReimburseSMO exportPubCarReimburseSMOImpl;

    /**
     * 导出用车报销信息
     * @param pd 前端传递参数封装
     * @return responseEntity
     */
    public ResponseEntity<Object> export(IPageData pd) {
        return exportPubCarReimburseSMOImpl.exportPubCarReimburse(pd);
    }

}

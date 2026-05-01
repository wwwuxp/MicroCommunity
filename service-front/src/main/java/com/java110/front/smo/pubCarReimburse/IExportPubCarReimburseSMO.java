package com.java110.front.smo.pubCarReimburse;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/10/27
 */
public interface IExportPubCarReimburseSMO {

    /**
     * 导出用车报销信息
     * @param pd 前端传递参数封装
     * @return responseEntity
     */
    ResponseEntity<Object> exportPubCarReimburse(IPageData pd);

}

package com.java110.front.smo.pubCarRepair;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改车辆维修信息表接口
 * add by hh
 */
public interface IEditPubCarRepairSMO {

    /**
     * 修改小区
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> updatePubCarRepair(IPageData pd);
}

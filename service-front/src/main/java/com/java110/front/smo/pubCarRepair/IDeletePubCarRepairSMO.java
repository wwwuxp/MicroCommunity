package com.java110.front.smo.pubCarRepair;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加车辆维修信息表接口
 * add by hh
 */
public interface IDeletePubCarRepairSMO {

    /**
     * 添加车辆维修信息表
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> deletePubCarRepair(IPageData pd);
}

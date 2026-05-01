package com.java110.front.smo.pubCar;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;
/**
 * 车辆管理表管理服务接口类
 *
 * add by hh
 */
public interface IListPubCarsSMO {

    /**
     * 查询车辆管理表信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listPubCars(IPageData pd) throws SMOException;

    /**
     * 获取车辆的简单信息
     * @param pd
     * @return
     */
    ResponseEntity<String> getSimplePubCars(IPageData pd);

}

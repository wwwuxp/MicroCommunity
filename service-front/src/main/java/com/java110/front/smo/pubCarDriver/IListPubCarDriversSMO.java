package com.java110.front.smo.pubCarDriver;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;
/**
 * 司机信息表管理服务接口类
 * add by hh
 */
public interface IListPubCarDriversSMO {

    /**
     * 查询司机信息表信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listPubCarDrivers(IPageData pd) throws SMOException;

    ResponseEntity<String> getPubCarDriverNames(IPageData pd);

}

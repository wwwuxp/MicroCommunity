package com.java110.front.smo.pubCarGroup;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 分组信息表管理服务接口类
 * add by hh
 */
public interface IListPubCarGroupsSMO {

    /**
     * 查询分组信息表信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listPubCarGroups(IPageData pd) throws SMOException;

    ResponseEntity<String> listPubCarGroupNames(IPageData pd);

}

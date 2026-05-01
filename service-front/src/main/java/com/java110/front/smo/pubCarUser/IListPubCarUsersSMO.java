package com.java110.front.smo.pubCarUser;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 用车人管理表管理服务接口类
 * add by hh
 */
public interface IListPubCarUsersSMO {

    /**
     * 查询用车人管理表信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listPubCarUsers(IPageData pd) throws SMOException;

    /**
     * 选择用户列表
     * @param pd 封装数据
     * @return responseEntity
     */
    ResponseEntity<String> listPubCarUserDetails(IPageData pd);

    /**
     * 查询部门名字
     * @param pd 封装数据
     * @return responseEntity
     */
    ResponseEntity<String> listDepartmentNames(IPageData pd);

}

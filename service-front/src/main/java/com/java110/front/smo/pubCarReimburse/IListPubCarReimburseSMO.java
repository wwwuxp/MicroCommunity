package com.java110.front.smo.pubCarReimburse;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/10/12
 */
public interface IListPubCarReimburseSMO {

    /**
     * 查询用车报销列表信息
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> listPubCarReimburse(IPageData pd);

}

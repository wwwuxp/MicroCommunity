package com.java110.front.smo.pubCarApprover;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/12/13
 */
public interface IDeletePubCarApproverSMO {

    /**
     * 删除用车审批人信息
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> deletePubCarApprover(IPageData pd);

}

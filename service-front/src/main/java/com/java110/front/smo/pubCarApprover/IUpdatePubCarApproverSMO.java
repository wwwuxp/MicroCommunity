package com.java110.front.smo.pubCarApprover;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2022/3/29
 */
public interface IUpdatePubCarApproverSMO {

    /**
     * 修改公车审批人信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    ResponseEntity<String> updatePubCarApprover(IPageData pd);

}

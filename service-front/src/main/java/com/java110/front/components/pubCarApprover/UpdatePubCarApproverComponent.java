package com.java110.front.components.pubCarApprover;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarApprover.IUpdatePubCarApproverSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2022/3/29
 */
@Component("updatePubCarApprover")
public class UpdatePubCarApproverComponent {

    @Autowired
    private IUpdatePubCarApproverSMO updatePubCarApproverSMOImpl;

    /**
     * 修改公车审批人信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> update(IPageData pd){
        return updatePubCarApproverSMOImpl.updatePubCarApprover(pd);
    }

}

package com.java110.front.components.pubCarApprover;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarApprover.IAddPubCarApproverSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加公车审核人信息
 * @author hh
 * @date 2021/12/13
 */
@Component("addPubCarApprover")
public class AddPubCarApproverComponent {

    @Autowired
    private IAddPubCarApproverSMO addPubCarApproverSMOImpl;

    /**
     * 保存公车审核人信息
     * @param pd 前端请求参数封装
     * @return responseEntity
     */
    public ResponseEntity<String> save(IPageData pd){
        return addPubCarApproverSMOImpl.addPubCarApprover(pd);
    }

}

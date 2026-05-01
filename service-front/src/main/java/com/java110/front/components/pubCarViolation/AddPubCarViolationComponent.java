package com.java110.front.components.pubCarViolation;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarViolation.IAddPubCarViolationSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加车辆违章信息表组件
 * add by hh
 */
@Component("addPubCarViolation")
public class AddPubCarViolationComponent {

    @Autowired
    private IAddPubCarViolationSMO addPubCarViolationSMOImpl;

    /**
     * 添加车辆违章信息表数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addPubCarViolationSMOImpl.savePubCarViolation(pd);
    }

}

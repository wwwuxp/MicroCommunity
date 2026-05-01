package com.java110.front.components.pubCarViolation;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarViolation.IListPubCarViolationsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 应用组件管理类
 * add by hh
 */
@Component("choosePubCarViolation")
public class ChoosePubCarViolationComponent {

    @Autowired
    private IListPubCarViolationsSMO listPubCarViolationsSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarViolationsSMOImpl.listPubCarViolations(pd);
    }

}

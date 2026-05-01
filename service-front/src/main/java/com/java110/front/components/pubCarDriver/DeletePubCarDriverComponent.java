package com.java110.front.components.pubCarDriver;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarDriver.IDeletePubCarDriverSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加司机信息表组件
 */
@Component("deletePubCarDriver")
public class DeletePubCarDriverComponent {

    @Autowired
    private IDeletePubCarDriverSMO deletePubCarDriverSMOImpl;

    /**
     * 添加司机信息表数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deletePubCarDriverSMOImpl.deletePubCarDriver(pd);
    }

}

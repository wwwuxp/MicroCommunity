package com.java110.front.components.keyCabinetSn;

import com.java110.core.context.IPageData;
import com.java110.front.smo.keyCabinetSn.IAddKeyCabinetSnSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2022/2/18
 */
@Component("addKeyCabinetSn")
public class AddKeyCabinetSnComponent {

    @Autowired
    private IAddKeyCabinetSnSMO addKeyCabinetSnSMOImpl;

    /**
     * 保存钥匙柜信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> save(IPageData pd){
        return addKeyCabinetSnSMOImpl.addKeyCabinetSn(pd);
    }

}

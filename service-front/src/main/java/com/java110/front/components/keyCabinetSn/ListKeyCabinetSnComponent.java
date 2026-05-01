package com.java110.front.components.keyCabinetSn;

import com.java110.core.context.IPageData;
import com.java110.front.smo.keyCabinetSn.IListKeyCabinetSnSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2022/2/19
 */
@Component("listKeyCabinetSn")
public class ListKeyCabinetSnComponent {

    @Autowired
    private IListKeyCabinetSnSMO listKeyCabinetSnSMOImpl;

    /**
     * 查询钥匙柜设备信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listKeyCabinetSnSMOImpl.listKeyCabinetSn(pd);
    }

}

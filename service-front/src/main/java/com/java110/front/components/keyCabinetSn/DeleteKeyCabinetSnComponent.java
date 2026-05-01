package com.java110.front.components.keyCabinetSn;

import com.java110.core.context.IPageData;
import com.java110.front.smo.keyCabinetSn.IDeleteKeyCabinetSnSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2022/2/19
 */
@Component("deleteKeyCabinetSn")
public class DeleteKeyCabinetSnComponent {

    @Autowired
    private IDeleteKeyCabinetSnSMO deleteKeyCabinetSnSMOImpl;

    /**
     * 删除钥匙柜信息
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> delete(IPageData pd){
        return deleteKeyCabinetSnSMOImpl.deleteKeyCabinetSn(pd);
    }
}

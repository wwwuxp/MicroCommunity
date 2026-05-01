package com.java110.front.components.keyCabinetLog;

import com.java110.core.context.IPageData;
import com.java110.front.smo.keyCabinetLog.IListKeyCabinetLogSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @date 2022/2/14
 */
@Component("listKeyCabinetLog")
public class ListKeyCabinetLogComponent {

    @Autowired
    private IListKeyCabinetLogSMO listkeyCabinetLogSMOImpl;

    /**
     * 查询取换钥匙日志记录
     * @param pd 前端请求参数封装
     * @return ResposneEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listkeyCabinetLogSMOImpl.listKeyCabinetLog(pd);
    }

}

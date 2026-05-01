package com.java110.front.components.attendanceManagement;

import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListPassRecordSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询同行记录数据
 * @author hh
 * @date 2022/1/26
 */
@Component("listPassRecord")
public class ListPassRecordComponent {

    @Autowired
    private IListPassRecordSMO listPassRecordSMOImpl;

    /**
     * 查询通行记录
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> list(IPageData pd){
        return listPassRecordSMOImpl.listPassRecord(pd);
    }

}

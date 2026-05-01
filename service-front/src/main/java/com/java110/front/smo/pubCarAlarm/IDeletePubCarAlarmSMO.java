package com.java110.front.smo.pubCarAlarm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加异常报警信息表接口
 * add by hh
 */
public interface IDeletePubCarAlarmSMO {

    /**
     * 添加异常报警信息表
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> deletePubCarAlarm(IPageData pd);
}

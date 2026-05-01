package com.java110.front.smo.pubCarAlarm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 添加异常报警信息表接口
 * add by hh
 */
public interface IAddPubCarAlarmSMO {

    /**
     * 添加异常报警信息表
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> savePubCarAlarm(IPageData pd);
}

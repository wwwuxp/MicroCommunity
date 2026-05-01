package com.java110.front.smo.pubCarAlarm;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * 修改异常报警信息表接口
 * add by hh
 */
public interface IEditPubCarAlarmSMO {

    ResponseEntity<String> handleExceptionAlarm(IPageData pd);

}

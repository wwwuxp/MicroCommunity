package com.java110.front.smo.pubCarAccident;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @date 2021/10/11
 */
public interface IListPubCarAccidentSMO {

    /**
     * 查询用车事故列表
     * @param pd 请求参数封装
     * @return responseEntity
     */
    ResponseEntity<String> listPubCarAccident(IPageData pd);

}

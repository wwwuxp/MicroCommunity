package com.java110.front.components.pubCarAccident;

import com.java110.core.context.IPageData;
import com.java110.front.smo.pubCarAccident.IListPubCarAccidentSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询用车事故列表
 * @author hh
 * @date 2021/10/11
 */
@Component("listPubCarAccident")
public class ListPubCarAccidentComponent {

    @Autowired
    private IListPubCarAccidentSMO listPubCarAccidentSMOImpl;

    /**
     * 查询用车事故列表
     * @param pd 请求数据封装
     * @return responseEntity
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listPubCarAccidentSMOImpl.listPubCarAccident(pd);
    }


}

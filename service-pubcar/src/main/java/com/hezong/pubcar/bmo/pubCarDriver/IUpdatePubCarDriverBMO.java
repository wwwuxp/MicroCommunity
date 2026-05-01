package com.hezong.pubcar.bmo.pubCarDriver;

import com.java110.po.pubCarDriver.PubCarDriverPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePubCarDriverBMO {

    /**
     * 修改用车人管理表
     * add by hh
     * @param pubCarDriverPo
     * @return
     */
    ResponseEntity<String> update(PubCarDriverPo pubCarDriverPo);

}

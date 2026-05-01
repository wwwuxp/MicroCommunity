package com.hezong.pubcar.bmo.pubCarGroup;

import com.java110.po.pubCarGroup.PubCarGroupPo;
import org.springframework.http.ResponseEntity;

public interface ISavePubCarGroupBMO {

    /**
     * 添加分组信息表
     * add by hh
     * @param pubCarGroupPo
     * @return
     */
    ResponseEntity<String> save(PubCarGroupPo pubCarGroupPo);


}

package com.hezong.pubcar.bmo.pubCarGroup;

import com.java110.po.pubCarGroup.PubCarGroupPo;
import org.springframework.http.ResponseEntity;

public interface IDeletePubCarGroupBMO {

    /**
     * 修改分组信息表
     * add by hh
     * @param pubCarGroupPo
     * @return
     */
    ResponseEntity<String> delete(PubCarGroupPo pubCarGroupPo);


}

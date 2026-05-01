package com.hezong.pubcar.bmo.pubCarViolation;

import com.java110.po.pubCarViolation.PubCarViolationPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePubCarViolationBMO {

    /**
     * 修改车辆违章信息表
     * add by hh
     *
     * @param pubCarViolationPo
     * @return
     */
    ResponseEntity<String> update(PubCarViolationPo pubCarViolationPo);

}

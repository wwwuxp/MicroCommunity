package com.hezong.pubcar.bmo.pubCarViolation;

import com.java110.po.pubCarViolation.PubCarViolationPo;
import org.springframework.http.ResponseEntity;
public interface ISavePubCarViolationBMO {


    /**
     * 添加车辆违章信息表
     * add by hh
     * @param pubCarViolationPo
     * @return
     */
    ResponseEntity<String> save(PubCarViolationPo pubCarViolationPo);


}

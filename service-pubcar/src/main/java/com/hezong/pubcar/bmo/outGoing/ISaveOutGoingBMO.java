package com.hezong.pubcar.bmo.outGoing;

import com.java110.po.outGoing.OutGoingPo;
import org.springframework.http.ResponseEntity;

public interface ISaveOutGoingBMO {

    /**
     * 添加外出信息表
     * add by hh
     * @param outGoingPo
     * @return
     */
    ResponseEntity<String> save(OutGoingPo outGoingPo);


}

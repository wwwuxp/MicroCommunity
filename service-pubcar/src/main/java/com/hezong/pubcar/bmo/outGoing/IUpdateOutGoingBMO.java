package com.hezong.pubcar.bmo.outGoing;

import com.java110.po.outGoing.OutGoingPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateOutGoingBMO {

    /**
     * 修改外出信息表
     * add by hh
     * @param outGoingPo
     * @return
     */
    ResponseEntity<String> update(OutGoingPo outGoingPo);

}

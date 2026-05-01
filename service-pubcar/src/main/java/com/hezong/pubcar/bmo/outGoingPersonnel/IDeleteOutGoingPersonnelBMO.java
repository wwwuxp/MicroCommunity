package com.hezong.pubcar.bmo.outGoingPersonnel;

import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOutGoingPersonnelBMO {

    /**
     * 修改外出人员信息表
     * add by hh
     * @param outGoingPersonnelPo
     * @return
     */
    ResponseEntity<String> delete(OutGoingPersonnelPo outGoingPersonnelPo);


}

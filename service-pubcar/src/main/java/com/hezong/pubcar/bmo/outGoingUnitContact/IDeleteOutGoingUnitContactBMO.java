package com.hezong.pubcar.bmo.outGoingUnitContact;

import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOutGoingUnitContactBMO {

    /**
     * 修改外出单位联系人信息表
     * add by hh
     * @param outGoingUnitContactPo
     * @return
     */
    ResponseEntity<String> delete(OutGoingUnitContactPo outGoingUnitContactPo);


}

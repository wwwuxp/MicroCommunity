package com.hezong.pubcar.bmo.outGoingUnitContact.impl;

import com.hezong.pubcar.bmo.outGoingUnitContact.IUpdateOutGoingUnitContactBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IOutGoingUnitContactInnerServiceSMO;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateOutGoingUnitContactBMOImpl")
public class UpdateOutGoingUnitContactBMOImpl implements IUpdateOutGoingUnitContactBMO {

    @Autowired
    private IOutGoingUnitContactInnerServiceSMO outGoingUnitContactInnerServiceSMOImpl;

    /**
     * @param outGoingUnitContactPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(OutGoingUnitContactPo outGoingUnitContactPo) {

        int flag = outGoingUnitContactInnerServiceSMOImpl.updateOutGoingUnitContact(outGoingUnitContactPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

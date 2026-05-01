package com.hezong.pubcar.bmo.outGoingUnitContact.impl;

import com.hezong.pubcar.bmo.outGoingUnitContact.ISaveOutGoingUnitContactBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IOutGoingUnitContactInnerServiceSMO;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveOutGoingUnitContactBMOImpl")
public class SaveOutGoingUnitContactBMOImpl implements ISaveOutGoingUnitContactBMO {

    @Autowired
    private IOutGoingUnitContactInnerServiceSMO outGoingUnitContactInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param outGoingUnitContactPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(OutGoingUnitContactPo outGoingUnitContactPo) {

        outGoingUnitContactPo.setContactId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_UNIT_CONTACT_ID));
        int flag = outGoingUnitContactInnerServiceSMOImpl.saveOutGoingUnitContact(outGoingUnitContactPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.outGoingUnitContact.impl;

import com.hezong.pubcar.bmo.outGoingUnitContact.IDeleteOutGoingUnitContactBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IOutGoingUnitContactInnerServiceSMO;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOutGoingUnitContactBMOImpl")
public class DeleteOutGoingUnitContactBMOImpl implements IDeleteOutGoingUnitContactBMO {

    @Autowired
    private IOutGoingUnitContactInnerServiceSMO outGoingUnitContactInnerServiceSMOImpl;

    /**
     * @param outGoingUnitContactPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(OutGoingUnitContactPo outGoingUnitContactPo) {

        int flag = outGoingUnitContactInnerServiceSMOImpl.deleteOutGoingUnitContact(outGoingUnitContactPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

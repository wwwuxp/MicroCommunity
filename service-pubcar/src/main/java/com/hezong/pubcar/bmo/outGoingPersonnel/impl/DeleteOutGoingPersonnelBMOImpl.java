package com.hezong.pubcar.bmo.outGoingPersonnel.impl;

import com.hezong.pubcar.bmo.outGoingPersonnel.IDeleteOutGoingPersonnelBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOutGoingPersonnelBMOImpl")
public class DeleteOutGoingPersonnelBMOImpl implements IDeleteOutGoingPersonnelBMO {

    @Autowired
    private IOutGoingPersonnelInnerServiceSMO outGoingPersonnelInnerServiceSMOImpl;

    /**
     * @param outGoingPersonnelPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(OutGoingPersonnelPo outGoingPersonnelPo) {

        int flag = outGoingPersonnelInnerServiceSMOImpl.deleteOutGoingPersonnel(outGoingPersonnelPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

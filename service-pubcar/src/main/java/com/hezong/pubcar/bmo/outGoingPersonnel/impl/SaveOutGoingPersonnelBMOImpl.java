package com.hezong.pubcar.bmo.outGoingPersonnel.impl;

import com.hezong.pubcar.bmo.outGoingPersonnel.ISaveOutGoingPersonnelBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveOutGoingPersonnelBMOImpl")
public class SaveOutGoingPersonnelBMOImpl implements ISaveOutGoingPersonnelBMO {

    @Autowired
    private IOutGoingPersonnelInnerServiceSMO outGoingPersonnelInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param outGoingPersonnelPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(OutGoingPersonnelPo outGoingPersonnelPo) {

        outGoingPersonnelPo.setPersonnelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_PERSONNEL_ID));
        int flag = outGoingPersonnelInnerServiceSMOImpl.saveOutGoingPersonnel(outGoingPersonnelPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.outGoing.impl;

import com.hezong.pubcar.bmo.outGoing.ISaveOutGoingBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IOutGoingInnerServiceSMO;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveOutGoingBMOImpl")
public class SaveOutGoingBMOImpl implements ISaveOutGoingBMO {

    @Autowired
    private IOutGoingInnerServiceSMO outGoingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param outGoingPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(OutGoingPo outGoingPo) {

        outGoingPo.setOutId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_ID));
        int flag = outGoingInnerServiceSMOImpl.saveOutGoing(outGoingPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

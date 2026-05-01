package com.hezong.pubcar.bmo.pubCarViolation.impl;

import com.hezong.pubcar.bmo.pubCarViolation.IUpdatePubCarViolationBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarViolationInnerServiceSMO;
import com.java110.po.pubCarViolation.PubCarViolationPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePubCarViolationBMOImpl")
public class UpdatePubCarViolationBMOImpl implements IUpdatePubCarViolationBMO {

    @Autowired
    private IPubCarViolationInnerServiceSMO pubCarViolationInnerServiceSMOImpl;

    /**
     * @param pubCarViolationPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(PubCarViolationPo pubCarViolationPo) {

        int flag = pubCarViolationInnerServiceSMOImpl.updatePubCarViolation(pubCarViolationPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

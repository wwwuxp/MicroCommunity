package com.hezong.pubcar.bmo.pubCarViolation.impl;

import com.hezong.pubcar.bmo.pubCarViolation.ISavePubCarViolationBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarViolationInnerServiceSMO;
import com.java110.po.pubCarViolation.PubCarViolationPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarViolationBMOImpl")
public class SavePubCarViolationBMOImpl implements ISavePubCarViolationBMO {

    @Autowired
    private IPubCarViolationInnerServiceSMO pubCarViolationInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarViolationPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarViolationPo pubCarViolationPo) {

        pubCarViolationPo.setViolationId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_VIOLATION_ID));
        int flag = pubCarViolationInnerServiceSMOImpl.savePubCarViolation(pubCarViolationPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

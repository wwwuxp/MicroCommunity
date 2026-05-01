package com.hezong.pubcar.bmo.pubCarDriver.impl;

import com.hezong.pubcar.bmo.pubCarDriver.ISavePubCarDriverBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarDriverInnerServiceSMO;
import com.java110.po.pubCarDriver.PubCarDriverPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarDriverBMOImpl")
public class SavePubCarDriverBMOImpl implements ISavePubCarDriverBMO {

    @Autowired
    private IPubCarDriverInnerServiceSMO pubCarDriverInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarDriverPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarDriverPo pubCarDriverPo) {

        pubCarDriverPo.setUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userId));
        int flag = pubCarDriverInnerServiceSMOImpl.savePubCarDriver(pubCarDriverPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

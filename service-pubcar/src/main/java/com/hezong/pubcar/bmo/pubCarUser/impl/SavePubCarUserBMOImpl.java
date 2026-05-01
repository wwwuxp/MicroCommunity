package com.hezong.pubcar.bmo.pubCarUser.impl;

import com.hezong.pubcar.bmo.pubCarUser.ISavePubCarUserBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
import com.java110.po.pubCarUser.PubCarUserPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarUserBMOImpl")
public class SavePubCarUserBMOImpl implements ISavePubCarUserBMO {

    @Autowired
    private IPubCarUserInnerServiceSMO pubCarUserInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarUserPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarUserPo pubCarUserPo) {

        pubCarUserPo.setCarUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_CAR_USER_ID));
        int flag = pubCarUserInnerServiceSMOImpl.savePubCarUser(pubCarUserPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

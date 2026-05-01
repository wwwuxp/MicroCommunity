package com.hezong.pubcar.bmo.pubCarGroup.impl;

import com.hezong.pubcar.bmo.pubCarGroup.ISavePubCarGroupBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarGroupInnerServiceSMO;
import com.java110.po.pubCarGroup.PubCarGroupPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarGroupBMOImpl")
public class SavePubCarGroupBMOImpl implements ISavePubCarGroupBMO {

    @Autowired
    private IPubCarGroupInnerServiceSMO pubCarGroupInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarGroupPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarGroupPo pubCarGroupPo) {

        pubCarGroupPo.setGroupId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_groupId));
        int flag = pubCarGroupInnerServiceSMOImpl.savePubCarGroup(pubCarGroupPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.pubCarGroup.impl;

import com.hezong.pubcar.bmo.pubCarGroup.IUpdatePubCarGroupBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarGroupInnerServiceSMO;
import com.java110.po.pubCarGroup.PubCarGroupPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePubCarGroupBMOImpl")
public class UpdatePubCarGroupBMOImpl implements IUpdatePubCarGroupBMO {

    @Autowired
    private IPubCarGroupInnerServiceSMO pubCarGroupInnerServiceSMOImpl;

    /**
     * @param pubCarGroupPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(PubCarGroupPo pubCarGroupPo) {

        int flag = pubCarGroupInnerServiceSMOImpl.updatePubCarGroup(pubCarGroupPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.pubCarRepair.impl;

import com.hezong.pubcar.bmo.pubCarRepair.IUpdatePubCarRepairBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarRepairInnerServiceSMO;
import com.java110.po.pubCarRepair.PubCarRepairPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePubCarRepairBMOImpl")
public class UpdatePubCarRepairBMOImpl implements IUpdatePubCarRepairBMO {

    @Autowired
    private IPubCarRepairInnerServiceSMO pubCarRepairInnerServiceSMOImpl;

    /**
     * @param pubCarRepairPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(PubCarRepairPo pubCarRepairPo) {

        int flag = pubCarRepairInnerServiceSMOImpl.updatePubCarRepair(pubCarRepairPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

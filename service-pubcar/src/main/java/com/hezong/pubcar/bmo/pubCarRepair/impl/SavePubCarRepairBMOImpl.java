package com.hezong.pubcar.bmo.pubCarRepair.impl;

import com.hezong.pubcar.bmo.pubCarRepair.ISavePubCarRepairBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarRepairInnerServiceSMO;
import com.java110.po.pubCarRepair.PubCarRepairPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePubCarRepairBMOImpl")
public class SavePubCarRepairBMOImpl implements ISavePubCarRepairBMO {

    @Autowired
    private IPubCarRepairInnerServiceSMO pubCarRepairInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param pubCarRepairPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> save(PubCarRepairPo pubCarRepairPo) {

        pubCarRepairPo.setRepairId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_repairId));
        int flag = pubCarRepairInnerServiceSMOImpl.savePubCarRepair(pubCarRepairPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.hezong.pubcar.bmo.pubCarAlarm.impl;

import com.hezong.pubcar.bmo.pubCarAlarm.IUpdatePubCarAlarmBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarAlarmInnerServiceSMO;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePubCarAlarmBMOImpl")
public class UpdatePubCarAlarmBMOImpl implements IUpdatePubCarAlarmBMO {

    @Autowired
    private IPubCarAlarmInnerServiceSMO pubCarAlarmInnerServiceSMOImpl;

    /**
     * @param pubCarAlarmPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> update(PubCarAlarmPo pubCarAlarmPo) {

        int flag = pubCarAlarmInnerServiceSMOImpl.updatePubCarAlarm(pubCarAlarmPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

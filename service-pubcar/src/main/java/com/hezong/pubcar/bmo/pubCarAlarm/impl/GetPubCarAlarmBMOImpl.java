package com.hezong.pubcar.bmo.pubCarAlarm.impl;

import com.hezong.pubcar.bmo.pubCarAlarm.IGetPubCarAlarmBMO;
import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import com.java110.intf.pubcar.IPubCarAlarmInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarAlarmBMOImpl")
public class GetPubCarAlarmBMOImpl implements IGetPubCarAlarmBMO {

    @Autowired
    private IPubCarAlarmInnerServiceSMO pubCarAlarmInnerServiceSMOImpl;

    /**
     * @param  pubCarAlarmDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarAlarmDto pubCarAlarmDto) {


        int count = pubCarAlarmInnerServiceSMOImpl.queryPubCarAlarmsCount(pubCarAlarmDto);
//
        List<PubCarAlarmDto> pubCarAlarmDtos = null;
//        if (count > 0) {
//            pubCarAlarmDtos = pubCarAlarmInnerServiceSMOImpl.queryPubCarAlarms(pubCarAlarmDto);
//        } else {
//            pubCarAlarmDtos = new ArrayList<>();
//        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarAlarmDto.getRow()), count, pubCarAlarmDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

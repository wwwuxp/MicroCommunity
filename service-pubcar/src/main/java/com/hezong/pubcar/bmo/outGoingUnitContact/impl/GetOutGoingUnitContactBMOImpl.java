package com.hezong.pubcar.bmo.outGoingUnitContact.impl;

import com.hezong.pubcar.bmo.outGoingUnitContact.IGetOutGoingUnitContactBMO;
import com.java110.intf.pubcar.IOutGoingUnitContactInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.outGoingUnitContact.OutGoingUnitContactDto;

import java.util.ArrayList;
import java.util.List;

@Service("getOutGoingUnitContactBMOImpl")
public class GetOutGoingUnitContactBMOImpl implements IGetOutGoingUnitContactBMO {

    @Autowired
    private IOutGoingUnitContactInnerServiceSMO outGoingUnitContactInnerServiceSMOImpl;

    /**
     * @param  outGoingUnitContactDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(OutGoingUnitContactDto outGoingUnitContactDto) {


        int count = outGoingUnitContactInnerServiceSMOImpl.queryOutGoingUnitContactsCount(outGoingUnitContactDto);

        List<OutGoingUnitContactDto> outGoingUnitContactDtos = null;
        if (count > 0) {
            outGoingUnitContactDtos = outGoingUnitContactInnerServiceSMOImpl.queryOutGoingUnitContacts(outGoingUnitContactDto);
        } else {
            outGoingUnitContactDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) outGoingUnitContactDto.getRow()), count, outGoingUnitContactDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

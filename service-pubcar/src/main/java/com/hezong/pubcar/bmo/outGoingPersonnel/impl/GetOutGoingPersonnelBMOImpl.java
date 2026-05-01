package com.hezong.pubcar.bmo.outGoingPersonnel.impl;

import com.hezong.pubcar.bmo.outGoingPersonnel.IGetOutGoingPersonnelBMO;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.outGoingPersonnel.OutGoingPersonnelDto;

import java.util.ArrayList;
import java.util.List;

@Service("getOutGoingPersonnelBMOImpl")
public class GetOutGoingPersonnelBMOImpl implements IGetOutGoingPersonnelBMO {

    @Autowired
    private IOutGoingPersonnelInnerServiceSMO outGoingPersonnelInnerServiceSMOImpl;

    /**
     * @param  outGoingPersonnelDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(OutGoingPersonnelDto outGoingPersonnelDto) {


        int count = outGoingPersonnelInnerServiceSMOImpl.queryOutGoingPersonnelsCount(outGoingPersonnelDto);

        List<OutGoingPersonnelDto> outGoingPersonnelDtos = null;
        if (count > 0) {
            outGoingPersonnelDtos = outGoingPersonnelInnerServiceSMOImpl.queryOutGoingPersonnels(outGoingPersonnelDto);
        } else {
            outGoingPersonnelDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) outGoingPersonnelDto.getRow()), count, outGoingPersonnelDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

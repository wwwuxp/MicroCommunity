package com.hezong.pubcar.bmo.outGoing.impl;

import com.hezong.pubcar.bmo.outGoing.IGetOutGoingBMO;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.intf.pubcar.IOutGoingInnerServiceSMO;
import com.java110.vo.ResultVo;
import com.java110.vo.api.outGoing.ApiOutGoingDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getOutGoingBMOImpl")
public class GetOutGoingBMOImpl implements IGetOutGoingBMO {

    @Autowired
    private IOutGoingInnerServiceSMO outGoingInnerServiceSMOImpl;

    /**
     * @param  outGoingDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(OutGoingDto outGoingDto) {


        int count = outGoingInnerServiceSMOImpl.queryOutGoingsCount(outGoingDto);

        List<ApiOutGoingDataVo> outGoingDtos = null;
        if (count > 0) {
            outGoingDtos = outGoingInnerServiceSMOImpl.queryOutGoings(outGoingDto);
        } else {
            outGoingDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) outGoingDto.getRow()), count, outGoingDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

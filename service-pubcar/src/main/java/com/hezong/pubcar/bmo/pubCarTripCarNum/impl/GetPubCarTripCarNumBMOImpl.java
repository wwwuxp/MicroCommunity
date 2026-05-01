package com.hezong.pubcar.bmo.pubCarTripCarNum.impl;

import com.hezong.pubcar.bmo.pubCarTripCarNum.IGetPubCarTripCarNumBMO;
import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import com.java110.intf.pubcar.IPubCarTripCarNumInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarTripCarNumBMOImpl")
public class GetPubCarTripCarNumBMOImpl implements IGetPubCarTripCarNumBMO {

    @Autowired
    private IPubCarTripCarNumInnerServiceSMO pubCarTripCarNumInnerServiceSMOImpl;

    /**
     * @param  pubCarTripCarNumDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarTripCarNumDto pubCarTripCarNumDto) {


        int count = pubCarTripCarNumInnerServiceSMOImpl.queryPubCarTripCarNumsCount(pubCarTripCarNumDto);

        List<PubCarTripCarNumDto> pubCarTripCarNumDtos = null;
        if (count > 0) {
            pubCarTripCarNumDtos = pubCarTripCarNumInnerServiceSMOImpl.queryPubCarTripCarNums(pubCarTripCarNumDto);
        } else {
            pubCarTripCarNumDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarTripCarNumDto.getRow()), count, pubCarTripCarNumDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

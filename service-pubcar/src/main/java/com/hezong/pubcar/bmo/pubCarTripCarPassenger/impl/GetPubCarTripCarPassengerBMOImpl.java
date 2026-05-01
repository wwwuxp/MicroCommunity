package com.hezong.pubcar.bmo.pubCarTripCarPassenger.impl;

import com.hezong.pubcar.bmo.pubCarTripCarPassenger.IGetPubCarTripCarPassengerBMO;
import com.java110.dto.pubCarTripCarPassenger.PubCarTripCarPassengerDto;
import com.java110.intf.pubcar.IPubCarTripCarPassengerInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarTripCarPassengerBMOImpl")
public class GetPubCarTripCarPassengerBMOImpl implements IGetPubCarTripCarPassengerBMO {

    @Autowired
    private IPubCarTripCarPassengerInnerServiceSMO pubCarTripCarPassengerInnerServiceSMOImpl;

    /**
     * @param  pubCarTripCarPassengerDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PubCarTripCarPassengerDto pubCarTripCarPassengerDto) {


        int count = pubCarTripCarPassengerInnerServiceSMOImpl.queryPubCarTripCarPassengersCount(pubCarTripCarPassengerDto);

        List<PubCarTripCarPassengerDto> pubCarTripCarPassengerDtos = null;
        if (count > 0) {
            pubCarTripCarPassengerDtos = pubCarTripCarPassengerInnerServiceSMOImpl.queryPubCarTripCarPassengers(pubCarTripCarPassengerDto);
        } else {
            pubCarTripCarPassengerDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarTripCarPassengerDto.getRow()), count, pubCarTripCarPassengerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

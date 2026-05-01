package com.hezong.pubcar.bmo.pubCarTrip.impl;

import com.hezong.pubcar.bmo.pubCarTrip.IGetPubCarTripBMO;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.vo.ResultVo;
import com.java110.vo.api.pubCarTrip.ApiPubCarTripDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarTripBMOImpl")
public class GetPubCarTripBMOImpl implements IGetPubCarTripBMO {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    /**
     * @param  pubCarTripDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarTripDto pubCarTripDto) {


        int count = pubCarTripInnerServiceSMOImpl.queryPubCarTripsCount(pubCarTripDto);

        List<ApiPubCarTripDataVo> pubCarTripDtos = null;
        if (count > 0) {
            pubCarTripDtos = pubCarTripInnerServiceSMOImpl.queryPubCarTrips(pubCarTripDto);
        } else {
            pubCarTripDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarTripDto.getRow()), count, pubCarTripDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

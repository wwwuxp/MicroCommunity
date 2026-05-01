package com.hezong.pubcar.bmo.pubCarTripMileage.impl;

import com.hezong.pubcar.bmo.pubCarTripMileage.IGetPubCarTripMileageBMO;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarTripMileageBMOImpl")
public class GetPubCarTripMileageBMOImpl implements IGetPubCarTripMileageBMO {

    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;

    /**
     * @param  pubCarTripMileageDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarTripMileageDto pubCarTripMileageDto) {


        int count = pubCarTripMileageInnerServiceSMOImpl.queryPubCarTripMileagesCount(pubCarTripMileageDto);

        List<PubCarTripMileageDto> pubCarTripMileageDtos = null;
        if (count > 0) {
            pubCarTripMileageDtos = pubCarTripMileageInnerServiceSMOImpl.queryPubCarTripMileages(pubCarTripMileageDto);
        } else {
            pubCarTripMileageDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarTripMileageDto.getRow()), count, pubCarTripMileageDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

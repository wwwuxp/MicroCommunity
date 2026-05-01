package com.hezong.pubcar.bmo.pubCarDriver.impl;

import com.hezong.pubcar.bmo.pubCarDriver.IGetPubCarDriverBMO;
import com.java110.intf.pubcar.IPubCarDriverInnerServiceSMO;
import com.java110.vo.ResultVo;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarDriverDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.pubCarDriver.PubCarDriverDto;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarDriverBMOImpl")
public class GetPubCarDriverBMOImpl implements IGetPubCarDriverBMO {

    @Autowired
    private IPubCarDriverInnerServiceSMO pubCarDriverInnerServiceSMOImpl;

    /**
     * @param  pubCarDriverDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarDriverDto pubCarDriverDto) {


        int count = pubCarDriverInnerServiceSMOImpl.queryPubCarDriversCount(pubCarDriverDto);

        List<ApiPubCarDriverDataVo> pubCarDriverDtos = null;
        if (count > 0) {
            pubCarDriverDtos = pubCarDriverInnerServiceSMOImpl.queryPubCarDrivers(pubCarDriverDto);
        } else {
            pubCarDriverDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarDriverDto.getRow()), count, pubCarDriverDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

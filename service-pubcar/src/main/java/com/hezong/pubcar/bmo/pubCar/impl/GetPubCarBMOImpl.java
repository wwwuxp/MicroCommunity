package com.hezong.pubcar.bmo.pubCar.impl;

import com.hezong.pubcar.bmo.pubCar.IGetPubCarBMO;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.pubCar.PubCarDto;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarBMOImpl")
public class GetPubCarBMOImpl implements IGetPubCarBMO {

    @Autowired
    private IPubCarInnerServiceSMO pubCarInnerServiceSMOImpl;

    /**
     *
     *
     * @param  pubCarDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarDto pubCarDto) {


        int count = pubCarInnerServiceSMOImpl.queryPubCarsCount(pubCarDto);

        List<PubCarDto> pubCarDtos;
        if (count > 0) {
            pubCarDtos = pubCarInnerServiceSMOImpl.queryPubCars(pubCarDto);
        } else {
            pubCarDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarDto.getRow()), count, pubCarDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

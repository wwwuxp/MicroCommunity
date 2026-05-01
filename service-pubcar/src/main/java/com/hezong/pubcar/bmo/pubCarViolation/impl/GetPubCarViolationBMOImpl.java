package com.hezong.pubcar.bmo.pubCarViolation.impl;

import com.hezong.pubcar.bmo.pubCarViolation.IGetPubCarViolationBMO;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.intf.pubcar.IPubCarViolationInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("getPubCarViolationBMOImpl")
public class GetPubCarViolationBMOImpl implements IGetPubCarViolationBMO {

    @Autowired
    private IPubCarViolationInnerServiceSMO pubCarViolationInnerServiceSMOImpl;

    /**
     * @param  pubCarViolationDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarViolationDto pubCarViolationDto) {


        int count = pubCarViolationInnerServiceSMOImpl.queryPubCarViolationsCount(pubCarViolationDto);

        List<PubCarViolationDto> pubCarViolationDtos = null;
        // if (count > 0) {
        //     pubCarViolationDtos = pubCarViolationInnerServiceSMOImpl.queryPubCarViolations(pubCarViolationDto);
        // } else {
        //     pubCarViolationDtos = new ArrayList<>();
        // }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarViolationDto.getRow()), count, pubCarViolationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

package com.hezong.pubcar.bmo.pubCarRepair.impl;

import com.hezong.pubcar.bmo.pubCarRepair.IGetPubCarRepairBMO;
import com.java110.dto.pubCarRepair.PubCarRepairDto;
import com.java110.intf.pubcar.IPubCarRepairInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarRepairBMOImpl")
public class GetPubCarRepairBMOImpl implements IGetPubCarRepairBMO {

    @Autowired
    private IPubCarRepairInnerServiceSMO pubCarRepairInnerServiceSMOImpl;

    /**
     * @param  pubCarRepairDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarRepairDto pubCarRepairDto) {


        int count = pubCarRepairInnerServiceSMOImpl.queryPubCarRepairsCount(pubCarRepairDto);

        List<PubCarRepairDto> pubCarRepairDtos = null;
        if (count > 0) {
            pubCarRepairDtos = pubCarRepairInnerServiceSMOImpl.queryPubCarRepairs(pubCarRepairDto);
        } else {
            pubCarRepairDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarRepairDto.getRow()), count, pubCarRepairDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

package com.hezong.pubcar.bmo.pubCarGroup.impl;

import com.hezong.pubcar.bmo.pubCarGroup.IGetPubCarGroupBMO;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.intf.pubcar.IPubCarGroupInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPubCarGroupBMOImpl")
public class GetPubCarGroupBMOImpl implements IGetPubCarGroupBMO {

    @Autowired
    private IPubCarGroupInnerServiceSMO pubCarGroupInnerServiceSMOImpl;

    /**
     * @param  pubCarGroupDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(PubCarGroupDto pubCarGroupDto) {


        int count = pubCarGroupInnerServiceSMOImpl.queryPubCarGroupsCount(pubCarGroupDto);

        List<PubCarGroupDto> pubCarGroupDtos = null;
        if (count > 0) {
            pubCarGroupDtos = pubCarGroupInnerServiceSMOImpl.queryPubCarGroups(pubCarGroupDto);
        } else {
            pubCarGroupDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarGroupDto.getRow()), count, pubCarGroupDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

package com.hezong.pubcar.bmo.pubCarUser.impl;

import com.hezong.pubcar.bmo.pubCarUser.IGetPubCarUserBMO;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.pubCarUser.PubCarUserDto;
import java.util.ArrayList;
import java.util.List;

@Service("getPubCarUserBMOImpl")
public class GetPubCarUserBMOImpl implements IGetPubCarUserBMO {

    @Autowired
    private IPubCarUserInnerServiceSMO pubCarUserInnerServiceSMOImpl;

    /**
     * @param  pubCarUserDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(PubCarUserDto pubCarUserDto) {


        int count = pubCarUserInnerServiceSMOImpl.queryPubCarUsersCount(pubCarUserDto);

        List<PubCarUserDto> pubCarUserDtos = null;
        if (count > 0) {
            pubCarUserDtos = pubCarUserInnerServiceSMOImpl.queryPubCarUsers(pubCarUserDto);
        } else {
            pubCarUserDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) pubCarUserDto.getRow()), count, pubCarUserDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}

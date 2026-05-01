package com.hezong.pubcar.bmo.pubCarGroup;

import com.java110.dto.pubCarGroup.PubCarGroupDto;
import org.springframework.http.ResponseEntity;

public interface IGetPubCarGroupBMO {

    /**
     * 查询分组信息表
     * add by hh
     * @param  pubCarGroupDto
     * @return
     */
    ResponseEntity<String> get(PubCarGroupDto pubCarGroupDto);


}

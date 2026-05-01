package com.hezong.pubcar.bmo.pubCarDriver;
import com.java110.dto.pubCarDriver.PubCarDriverDto;
import org.springframework.http.ResponseEntity;
public interface IGetPubCarDriverBMO {


    /**
     * 查询用车人管理表
     * add by hh
     * @param  pubCarDriverDto
     * @return
     */
    ResponseEntity<String> get(PubCarDriverDto pubCarDriverDto);


}

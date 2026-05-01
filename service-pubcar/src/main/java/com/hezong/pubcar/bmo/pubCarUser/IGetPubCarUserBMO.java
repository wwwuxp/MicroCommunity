package com.hezong.pubcar.bmo.pubCarUser;
import com.java110.dto.pubCarUser.PubCarUserDto;
import org.springframework.http.ResponseEntity;
public interface IGetPubCarUserBMO {


    /**
     * 查询用车人管理表
     * add by hh
     * @param  pubCarUserDto
     * @return
     */
    ResponseEntity<String> get(PubCarUserDto pubCarUserDto);


}

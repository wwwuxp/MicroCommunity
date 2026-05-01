package com.hezong.pubcar.bmo.pubCar;
import com.java110.dto.pubCar.PubCarDto;
import org.springframework.http.ResponseEntity;

public interface IGetPubCarBMO {

    /**
     * 查询车辆管理表
     * add by wuxw
     * @param  pubCarDto
     * @return
     */
    ResponseEntity<String> get(PubCarDto pubCarDto);


}

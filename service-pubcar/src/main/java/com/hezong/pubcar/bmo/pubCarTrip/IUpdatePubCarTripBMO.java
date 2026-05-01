package com.hezong.pubcar.bmo.pubCarTrip;
import com.java110.po.pubCarTrip.PubCarTripPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePubCarTripBMO {

    /**
     * 修改行程管理表
     * add by hh
     * @param pubCarTripPo
     * @return
     */
    ResponseEntity<String> update(PubCarTripPo pubCarTripPo);

}

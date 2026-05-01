package com.hezong.pubcar.bmo.pubCarTripCarNum;

import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import org.springframework.http.ResponseEntity;
public interface ISavePubCarTripCarNumBMO {


    /**
     * 添加申请车辆数量管理表
     * add by hh
     * @param pubCarTripCarNumPo
     * @return
     */
    ResponseEntity<String> save(PubCarTripCarNumPo pubCarTripCarNumPo);


}

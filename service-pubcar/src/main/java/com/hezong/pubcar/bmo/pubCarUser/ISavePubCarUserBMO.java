package com.hezong.pubcar.bmo.pubCarUser;

import com.java110.po.pubCarUser.PubCarUserPo;
import org.springframework.http.ResponseEntity;

public interface ISavePubCarUserBMO {

    /**
     * 添加用车人管理表
     * add by hh
     * @param pubCarUserPo
     * @return
     */
    ResponseEntity<String> save(PubCarUserPo pubCarUserPo);


}

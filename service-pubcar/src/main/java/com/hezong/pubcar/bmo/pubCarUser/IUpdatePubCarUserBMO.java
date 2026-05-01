package com.hezong.pubcar.bmo.pubCarUser;
import com.java110.po.pubCarUser.PubCarUserPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePubCarUserBMO {

    /**
     * 修改用车人管理表
     * add by hh
     * @param pubCarUserPo
     * @return
     */
    ResponseEntity<String> update(PubCarUserPo pubCarUserPo);

}

package com.hezong.pubcar.bmo.pubCarUser.impl;

import com.hezong.pubcar.bmo.pubCarUser.IDeletePubCarUserBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
import com.java110.po.pubCarUser.PubCarUserPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("deletePubCarUserBMOImpl")
public class DeletePubCarUserBMOImpl implements IDeletePubCarUserBMO {

    @Autowired
    private IPubCarUserInnerServiceSMO pubCarUserInnerServiceSMOImpl;

    /**
     * @param pubCarUserPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(PubCarUserPo pubCarUserPo) {

        int flag = pubCarUserInnerServiceSMOImpl.deletePubCarUser(pubCarUserPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

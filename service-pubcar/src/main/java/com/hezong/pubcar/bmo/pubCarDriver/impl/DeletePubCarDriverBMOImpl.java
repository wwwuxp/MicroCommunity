package com.hezong.pubcar.bmo.pubCarDriver.impl;

import com.hezong.pubcar.bmo.pubCarDriver.IDeletePubCarDriverBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IPubCarDriverInnerServiceSMO;
import com.java110.po.pubCarDriver.PubCarDriverPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deletePubCarDriverBMOImpl")
public class DeletePubCarDriverBMOImpl implements IDeletePubCarDriverBMO {

    @Autowired
    private IPubCarDriverInnerServiceSMO pubCarDriverInnerServiceSMOImpl;

    /**
     * @param pubCarDriverPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(PubCarDriverPo pubCarDriverPo) {

        int flag = pubCarDriverInnerServiceSMOImpl.deletePubCarDriver(pubCarDriverPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

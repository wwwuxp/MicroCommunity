package com.hezong.pubcar.bmo.outGoing.impl;

import com.hezong.pubcar.bmo.outGoing.IDeleteOutGoingBMO;
import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.pubcar.IOutGoingInnerServiceSMO;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOutGoingBMOImpl")
public class DeleteOutGoingBMOImpl implements IDeleteOutGoingBMO {

    @Autowired
    private IOutGoingInnerServiceSMO outGoingInnerServiceSMOImpl;

    /**
     * @param outGoingPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @Java110Transactional
    public ResponseEntity<String> delete(OutGoingPo outGoingPo) {

        int flag = outGoingInnerServiceSMOImpl.deleteOutGoing(outGoingPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}

package com.java110.store.bmo.collection.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.intf.common.IGoodCollectionUserInnerServiceSMO;
import com.java110.intf.store.IPurchaseApplyInnerServiceSMO;
import com.java110.po.purchase.PurchaseApplyPo;
import com.java110.store.bmo.collection.IGoodsCollectionBMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("goodsCollectionBMOImpl")
public class GoodsCollectionBMOImpl implements IGoodsCollectionBMO {

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;


    @Autowired
    private IGoodCollectionUserInnerServiceSMO goodCollectionUserInnerServiceSMOImpl;


    @Override
    @Java110Transactional
    public ResponseEntity<String> collection(PurchaseApplyPo purchaseApplyPo) {

        int saveFlag = purchaseApplyInnerServiceSMOImpl.savePurchaseApply(purchaseApplyPo);

        if (saveFlag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "采购申请失败");
        }

        PurchaseApplyDto purchaseApplyDto = BeanConvertUtil.covertBean(purchaseApplyPo, PurchaseApplyDto.class);
        purchaseApplyDto.setCurrentUserId(purchaseApplyPo.getUserId());
        if(!purchaseApplyPo.getWarehousingWay().equals(PurchaseApplyDto.WAREHOUSING_TYPE_DIRECT)){
            try {
                goodCollectionUserInnerServiceSMOImpl.startProcess(purchaseApplyDto);
            }catch (Exception e){
                e.printStackTrace();
                String message = e.getMessage();
                if (StringUtils.isNotBlank(message) && message.startsWith("请求参数错误") && message.contains("{") && message.contains("}")){
                    JSONObject bodyJson = JSONObject.parseObject(message.substring(message.indexOf("{"), message.indexOf("}") + 1));
                    String newMessage = bodyJson.getString("message");
                    throw new BusinessException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, newMessage);
                }
                throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, message);
            }
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "物品领用成功");
    }
}

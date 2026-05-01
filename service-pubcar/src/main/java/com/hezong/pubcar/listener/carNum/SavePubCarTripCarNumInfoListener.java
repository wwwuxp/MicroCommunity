package com.hezong.pubcar.listener.carNum;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripCarNumServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存车辆数目信息 侦听
 * @author hh
 * @datetime 2021/7/26 15:49
 */
@Java110Listener("savePubCarTripCarNumInfoListener")
@Transactional
public class SavePubCarTripCarNumInfoListener extends AbstractPubCarTripCarNumBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripCarNumServiceDao pubCarTripCarNumServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarTripCarNumPo.class.getSimpleName())){
            Object obj = data.get(PubCarTripCarNumPo.class.getSimpleName());
            JSONArray businessTripCarNums = null;
            if(obj instanceof JSONObject){
                businessTripCarNums = new JSONArray();
                businessTripCarNums.add(obj);
            }else {
                businessTripCarNums = (JSONArray)obj;
            }
            for (int tripCarNumIndex = 0; tripCarNumIndex < businessTripCarNums.size();tripCarNumIndex++) {
                JSONObject businessTripCarNum = businessTripCarNums.getJSONObject(tripCarNumIndex);
                doBusinessTripCarNum(business, businessTripCarNum);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("numId", businessTripCarNum.getString("numId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        List<Map> businessTripCarNumInfo = pubCarTripCarNumServiceDaoImpl.getBusinessTripCarNumInfo(info);
        if( businessTripCarNumInfo != null && businessTripCarNumInfo.size() >0) {
            pubCarTripCarNumServiceDaoImpl.savePubCarTripCarNumInfo(info);
            if(businessTripCarNumInfo.size() == 1) {
                dataFlowContext.addParamOut("numId", businessTripCarNumInfo.get(0).get("numId"));
            }
        }
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map<String, String> info = new HashMap<>();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map<String, String> paramIn = new HashMap<>();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        // 车辆数目信息
        List<Map> pubCarDriverInfo = pubCarTripCarNumServiceDaoImpl.getPubCarTripCarNumInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarDriverInfo)) {
            pubCarTripCarNumServiceDaoImpl.updatePubCarTripCarNumInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP_CAR_NUM;
    }

    @Override
    public IPubCarTripCarNumServiceDao getPubCarTripCarNumServiceDaoImpl() {
        return pubCarTripCarNumServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarTripCarNum
     */
    private void doBusinessTripCarNum(Business business, JSONObject businessPubCarTripCarNum){

        Assert.jsonObjectHaveKey(businessPubCarTripCarNum,"numId","businessPubCarTripCarNum 节点下没有包含 numId 节点");

        if(businessPubCarTripCarNum.getString("numId").startsWith("-")){
            businessPubCarTripCarNum.put("numId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_CAR_NUM));
        }

        businessPubCarTripCarNum.put("bId",business.getbId());
        businessPubCarTripCarNum.put("operate", StatusConstant.OPERATE_ADD);
        // 保存业务信息
        pubCarTripCarNumServiceDaoImpl.saveBusinessPubCarTripCarNumInfo(businessPubCarTripCarNum);

    }
}

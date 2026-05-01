package com.hezong.pubcar.listener.passenger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripCarPassengerServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarTripCarPassenger.PubCarTripCarPassengerPo;
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
 * 保存车辆乘客信息 侦听
 * @author hh
 * @datetime 2021/7/26 15:49
 */
@Java110Listener("savePubCarTripCarPassengerInfoListener")
@Transactional
public class SavePubCarTripCarPassengerInfoListener extends AbstractPubCarTripCarPassengerBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripCarPassengerServiceDao pubCarTripCarPassengerServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarTripCarPassengerPo.class.getSimpleName())){
            Object obj = data.get(PubCarTripCarPassengerPo.class.getSimpleName());
            JSONArray businessCarPassengers = null;
            if(obj instanceof JSONObject){
                businessCarPassengers = new JSONArray();
                businessCarPassengers.add(obj);
            }else {
                businessCarPassengers = (JSONArray)obj;
            }
            for (int carPassengerIndex = 0; carPassengerIndex < businessCarPassengers.size();carPassengerIndex++) {
                JSONObject businessCarPassenger = businessCarPassengers.getJSONObject(carPassengerIndex);
                doBusinessCarPassenger(business, businessCarPassenger);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("passengerId", businessCarPassenger.getString("passengerId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        List<Map> businessTripCarPassengerInfo = pubCarTripCarPassengerServiceDaoImpl.getBusinessTripCarPassengerInfo(info);
        if( businessTripCarPassengerInfo != null && businessTripCarPassengerInfo.size() >0) {
            pubCarTripCarPassengerServiceDaoImpl.saveTripCarPassengerInfo(info);
            if(businessTripCarPassengerInfo.size() == 1) {
                dataFlowContext.addParamOut("passengerId", businessTripCarPassengerInfo.get(0).get("passengerId"));
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
        // 车辆乘客信息
        List<Map> pubCarTripCarPassengerInfo = pubCarTripCarPassengerServiceDaoImpl.getPubCarTripCarPassengerInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarTripCarPassengerInfo)) {
            pubCarTripCarPassengerServiceDaoImpl.updatePubCarTripCarPassengerInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP_CAR_PASSENGER;
    }

    @Override
    public IPubCarTripCarPassengerServiceDao getPubCarTripCarPassengerServiceDaoImpl() {
        return pubCarTripCarPassengerServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessCarPassenger
     */
    private void doBusinessCarPassenger(Business business, JSONObject businessCarPassenger){

        Assert.jsonObjectHaveKey(businessCarPassenger,"passengerId","businessCarPassenger 节点下没有包含 passengerId 节点");

        if(businessCarPassenger.getString("passengerId").startsWith("-")){
            businessCarPassenger.put("passengerId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_CAR_PASSENGER));
        }

        businessCarPassenger.put("bId", business.getbId());
        businessCarPassenger.put("operate", StatusConstant.OPERATE_ADD);
        // 保存业务信息
        pubCarTripCarPassengerServiceDaoImpl.saveBusinessTripCarPassengerInfo(businessCarPassenger);

    }
}

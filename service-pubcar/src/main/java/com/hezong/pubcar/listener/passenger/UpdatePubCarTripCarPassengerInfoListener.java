package com.hezong.pubcar.listener.passenger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripCarPassengerServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hh
 * @datetime 2021/7/23 10:48
 */
@Java110Listener("updatePubCarTripCarPassengerInfoListener")
@Transactional
public class UpdatePubCarTripCarPassengerInfoListener extends AbstractPubCarTripCarPassengerBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripCarPassengerServiceDao pubCarTripCarPassengerServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessPubCarUer 节点
        if(data.containsKey("pubCarDriver")){
            Object obj = data.get("pubCarDriver");
            JSONArray businessPubCarDrivers = null;
            if(obj instanceof JSONObject){
                businessPubCarDrivers = new JSONArray();
                businessPubCarDrivers.add(obj);
            }else {
                businessPubCarDrivers = (JSONArray)obj;
            }
            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarDrivers.size();pubCarDriverIndex++) {
                JSONObject businessPubCarDriver = businessPubCarDrivers.getJSONObject(pubCarDriverIndex);
                doBusinessPubCar(business, businessPubCarDriver);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("driverId", businessPubCarDriver.getString("driverId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);


//        List<Map> businessPubCarDriverInfos = pubCarTripCarPassengerServiceDaoImpl.getBusinessPubCarDriverInfo(info);
//        if (businessPubCarDriverInfos != null && businessPubCarDriverInfos.size() > 0) {
//            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarDriverInfos.size(); pubCarDriverIndex++) {
//
//                Map businessPubCarDriverInfo = businessPubCarDriverInfos.get(pubCarDriverIndex);
//                flushBusinessPubCarTripCarPassengerInfo(businessPubCarDriverInfo, StatusConstant.STATUS_CD_VALID);
//
//                pubCarTripCarPassengerServiceDaoImpl.updatePubCarDriverInfo(businessPubCarDriverInfo);
//                if (businessPubCarDriverInfo.size() == 1) {
//                    dataFlowContext.addParamOut("driverId", businessPubCarDriverInfo.get("driverId"));
//                }
//            }
//        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_DRIVER;
    }

    @Override
    public IPubCarTripCarPassengerServiceDao getPubCarTripCarPassengerServiceDaoImpl() {
        return pubCarTripCarPassengerServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarDriver
     */
    private void doBusinessPubCar(Business business,JSONObject businessPubCarDriver){

        Assert.jsonObjectHaveKey(businessPubCarDriver,"driverId","businessPubCarUser 节点下没有包含 driverId 节点");

        if(businessPubCarDriver.getString("driverId").startsWith("-")){
            businessPubCarDriver.put("driverId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_DRIVER_ID));
        }

        businessPubCarDriver.put("bId",business.getbId());
        businessPubCarDriver.put("operate", StatusConstant.OPERATE_MOD);

        // 保存用车人或司机业务信息
//        pubCarTripCarPassengerServiceDaoImpl.saveBusinessPubCar(businessPubCarDriver);

    }
}

package com.hezong.pubcar.listener.trip;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hh
 * @datetime 2021/7/23 10:48
 */
@Java110Listener("deletePubCarTripInfoListener")
@Transactional
public class DeletePubCarTripInfoListener extends AbstractPubCarTripBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripServiceDao pubCarTripServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

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

//        Map info = new HashMap();
//        info.put("bId", business.getbId());
//        info.put("operate", StatusConstant.OPERATE_DEL);
//
//
//        List<Map> businessPubCarDriverInfos = pubCarTripServiceDaoImpl.getBusinessPubCarDriverInfo(info);
//        if (businessPubCarDriverInfos != null && businessPubCarDriverInfos.size() > 0) {
//            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarDriverInfos.size(); pubCarDriverIndex++) {
//
//                Map businessPubCarInfo = businessPubCarDriverInfos.get(pubCarDriverIndex);
//                flushBusinessPubCarTripInfo(businessPubCarInfo, StatusConstant.STATUS_CD_INVALID);
//
//                pubCarTripServiceDaoImpl.updatePubCarDriverInfo(businessPubCarInfo);
//                if (businessPubCarInfo.size() == 1) {
//                    dataFlowContext.addParamOut("driverId", businessPubCarInfo.get("driverId"));
//                }
//            }
//        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_DRIVER;
    }

    @Override
    public IPubCarTripServiceDao getPubCarTripServiceDaoImpl() {
        return pubCarTripServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarDriver
     */
    private void doBusinessPubCar(Business business,JSONObject businessPubCarDriver){

        Assert.jsonObjectHaveKey(businessPubCarDriver,"driverId","businessPubCarDriver 节点下没有包含 driverId 节点");

        if(businessPubCarDriver.getString("driverId").startsWith("-")){
            businessPubCarDriver.put("driverId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_DRIVER_ID));
        }

        autoSaveDelBusinessPubCarDriver(business, businessPubCarDriver);

    }
}

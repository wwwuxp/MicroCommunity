package com.hezong.pubcar.listener.trip;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarTrip.PubCarTripPo;
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
 * 保存行程信息 侦听
 * @author hh
 * @datetime 2021/7/22 15:49
 */
@Java110Listener("savePubCarTripInfoListener")
@Transactional
public class SavePubCarTripInfoListener extends AbstractPubCarTripBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripServiceDao pubCarTripServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarTripPo.class.getSimpleName())){
            Object obj = data.get(PubCarTripPo.class.getSimpleName());
            JSONArray businessPubCarTrips = null;
            if(obj instanceof JSONObject){
                businessPubCarTrips = new JSONArray();
                businessPubCarTrips.add(obj);
            }else {
                businessPubCarTrips = (JSONArray)obj;
            }
            for (int pubCarTripIndex = 0; pubCarTripIndex < businessPubCarTrips.size();pubCarTripIndex++) {
                JSONObject businessPubCarTrip = businessPubCarTrips.getJSONObject(pubCarTripIndex);
                doBusinessPubCarTrip(business, businessPubCarTrip);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("tripId", businessPubCarTrip.getString("tripId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 申请用车信息
        List<Map> businessPubCarTripInfo = pubCarTripServiceDaoImpl.getBusinessPubCarTripInfo(info);
        if( businessPubCarTripInfo != null && businessPubCarTripInfo.size() >0) {
            pubCarTripServiceDaoImpl.savePubCarTripInfo(info);
            if(businessPubCarTripInfo.size() == 1) {
                dataFlowContext.addParamOut("tripId", businessPubCarTripInfo.get(0).get("tripId"));
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
        // 行程信息
        List<Map> pubCarDriverInfo = pubCarTripServiceDaoImpl.getPubCarTripInstanceInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarDriverInfo)) {
            pubCarTripServiceDaoImpl.updatePubCarTripInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP;
    }

    @Override
    public IPubCarTripServiceDao getPubCarTripServiceDaoImpl() {
        return pubCarTripServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarTrip
     */
    private void doBusinessPubCarTrip(Business business, JSONObject businessPubCarTrip){

        Assert.jsonObjectHaveKey(businessPubCarTrip,"tripId","businessPubCarTrip 节点下没有包含 tripId 节点");

        if(businessPubCarTrip.getString("tripId").startsWith("-")){
            businessPubCarTrip.put("tripId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_ID));
        }
        // 添加默认行程状态和默认审批状态
        businessPubCarTrip.put("tripStatus", StatusConstant.STATUS_NOT_STARTED);
        businessPubCarTrip.put("approvalStatus", StatusConstant.STATUS_RUNNING);

        businessPubCarTrip.put("bId",business.getbId());
        businessPubCarTrip.put("operate", StatusConstant.OPERATE_ADD);
        // 保存业务信息
        pubCarTripServiceDaoImpl.saveBusinessPubCarTripInfo(businessPubCarTrip);

    }
}

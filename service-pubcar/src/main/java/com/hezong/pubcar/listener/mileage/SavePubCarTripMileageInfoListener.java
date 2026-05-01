package com.hezong.pubcar.listener.mileage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripMileageServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
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
 * 保存行程里程信息
 * @author hh
 * @datetime 2021/7/22 15:49
 */
@Java110Listener("savePubCarTripMileageInfoListener")
@Transactional
public class SavePubCarTripMileageInfoListener extends AbstractPubCarTripMileageBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripMileageServiceDao pubCarTripMileageServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarTripMileagePo.class.getSimpleName())){
            Object obj = data.get(PubCarTripMileagePo.class.getSimpleName());
            JSONArray businessPubCarTripMileages = null;
            if(obj instanceof JSONObject){
                businessPubCarTripMileages = new JSONArray();
                businessPubCarTripMileages.add(obj);
            }else {
                businessPubCarTripMileages = (JSONArray)obj;
            }
            for (int pubCarTripMileageIndex = 0; pubCarTripMileageIndex < businessPubCarTripMileages.size();pubCarTripMileageIndex++) {
                JSONObject businessPubCarTripMileage = businessPubCarTripMileages.getJSONObject(pubCarTripMileageIndex);
                doBusinessTripMileage(business, businessPubCarTripMileage);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("mileageId", businessPubCarTripMileage.getString("mileageId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        List<Map> businessPubCarMileageInfo = pubCarTripMileageServiceDaoImpl.getBusinessPubCarTripMileageInfo(info);
        if( businessPubCarMileageInfo != null && businessPubCarMileageInfo.size() >0) {
            pubCarTripMileageServiceDaoImpl.savePubCarTripMileageInfo(info);
            if(businessPubCarMileageInfo.size() == 1) {
                dataFlowContext.addParamOut("mileageId", businessPubCarMileageInfo.get(0).get("mileageId"));
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
        // 行程里程信息
        List<Map> pubCarTripMileageInfo = pubCarTripMileageServiceDaoImpl.getPubCarTripMileageInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarTripMileageInfo)) {
            pubCarTripMileageServiceDaoImpl.updatePubCarTripMileageInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_TRIP_MILEAGE;
    }

    @Override
    public IPubCarTripMileageServiceDao getPubCarTripMileageServiceDaoImpl() {
        return pubCarTripMileageServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarTripMileage
     */
    private void doBusinessTripMileage(Business business, JSONObject businessPubCarTripMileage){

        Assert.jsonObjectHaveKey(businessPubCarTripMileage,"mileageId","businessPubCarTripMileage 节点下没有包含 mileageId 节点");

        if(businessPubCarTripMileage.getString("mileageId").startsWith("-")){
            businessPubCarTripMileage.put("mileageId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_MILEAGE));
        }

        businessPubCarTripMileage.put("bId",business.getbId());
        businessPubCarTripMileage.put("operate", StatusConstant.OPERATE_ADD);
        // 保存业务信息
        pubCarTripMileageServiceDaoImpl.saveBusinessPubCarTripMileageInfo(businessPubCarTripMileage);

    }
}

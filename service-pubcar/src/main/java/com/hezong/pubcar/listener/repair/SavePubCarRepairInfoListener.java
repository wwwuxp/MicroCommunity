package com.hezong.pubcar.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarRepairServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarRepair.PubCarRepairPo;
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
 * 保存车辆维修信息
 * @author hh
 * @datetime 2021/8/6 9:39
 */
@Java110Listener("savePubCarRepairInfoListener")
@Transactional
public class SavePubCarRepairInfoListener extends AbstractPubCarRepairBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarRepairServiceDao pubCarRepairServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarRepairPo.class.getSimpleName())){
            Object obj = data.get(PubCarRepairPo.class.getSimpleName());
            JSONArray businessPubCarRepairs = null;
            if(obj instanceof JSONObject){
                businessPubCarRepairs = new JSONArray();
                businessPubCarRepairs.add(obj);
            }else {
                businessPubCarRepairs = (JSONArray)obj;
            }
            for (int pubCarRepairIndex = 0; pubCarRepairIndex < businessPubCarRepairs.size();pubCarRepairIndex++) {
                JSONObject businessPubCarRepair = businessPubCarRepairs.getJSONObject(pubCarRepairIndex);
                doBusinessRepair(business, businessPubCarRepair);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("repairId", businessPubCarRepair.getString("repairId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 车辆维修信息
        List<Map> businessPubCarRepairInfo = pubCarRepairServiceDaoImpl.getBusinessPubCarRepairInfo(info);
        if( businessPubCarRepairInfo != null && businessPubCarRepairInfo.size() >0) {
            pubCarRepairServiceDaoImpl.savePubCarRepairInfo(info);
            if(businessPubCarRepairInfo.size() == 1) {
                dataFlowContext.addParamOut("repairId", businessPubCarRepairInfo.get(0).get("repairId"));
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
        // 车辆维修信息
        List<Map> pubCarRepairInfo = pubCarRepairServiceDaoImpl.getPubCarRepairInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarRepairInfo)) {
            pubCarRepairServiceDaoImpl.updatePubCarRepairInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_REPAIR;
    }

    @Override
    public IPubCarRepairServiceDao getPubCarRepairServiceDaoImpl() {
        return pubCarRepairServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarRepair
     */
    private void doBusinessRepair(Business business, JSONObject businessPubCarRepair){

        Assert.jsonObjectHaveKey(businessPubCarRepair,"repairId","businessPubCarRepair 节点下没有包含 repairId 节点");

        if(businessPubCarRepair.getString("repairId").startsWith("-")){
            businessPubCarRepair.put("repairId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_REPAIR_ID));
        }


        businessPubCarRepair.put("bId",business.getbId());
        businessPubCarRepair.put("operate", StatusConstant.OPERATE_ADD);
        // 保存车辆维修业务信息
        pubCarRepairServiceDaoImpl.saveBusinessPubCarRepairInfo(businessPubCarRepair);

    }
}

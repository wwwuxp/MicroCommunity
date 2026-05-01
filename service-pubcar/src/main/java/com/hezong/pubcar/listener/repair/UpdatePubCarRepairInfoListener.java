package com.hezong.pubcar.listener.repair;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarRepairServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarViolation.PubCarViolationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 此维修更新类暂时没用（里面一些内容暂时没有修改）
 * @author hh
 * @datetime 2021/8/6 9:39
 */
@Java110Listener("updatePubCarRepairInfoListener")
@Transactional
public class UpdatePubCarRepairInfoListener extends AbstractPubCarRepairBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarRepairServiceDao pubCarRepairServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessPubCarUer 节点
        if(data.containsKey(PubCarViolationPo.class.getSimpleName())){
            Object obj = data.get(PubCarViolationPo.class.getSimpleName());
            JSONArray businessPubCarViolations = null;
            if(obj instanceof JSONObject){
                businessPubCarViolations = new JSONArray();
                businessPubCarViolations.add(obj);
            }else {
                businessPubCarViolations = (JSONArray)obj;
            }
            for (int pubCarViolationIndex = 0; pubCarViolationIndex < businessPubCarViolations.size();pubCarViolationIndex++) {
                JSONObject businessPubCarViolation = businessPubCarViolations.getJSONObject(pubCarViolationIndex);
                doBusinessViolation(business, businessPubCarViolation);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("violationId", businessPubCarViolation.getString("violationId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);


//        List<Map> businessPubCarDriverInfos = pubCarViolationServiceDaoImpl.getBusinessPubCarDriverInfo(info);
//        if (businessPubCarDriverInfos != null && businessPubCarDriverInfos.size() > 0) {
//            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarDriverInfos.size(); pubCarDriverIndex++) {
//
//                Map businessPubCarDriverInfo = businessPubCarDriverInfos.get(pubCarDriverIndex);
//                flushBusinessPubCarViolationInfo(businessPubCarDriverInfo, StatusConstant.STATUS_CD_VALID);
//
//                pubCarViolationServiceDaoImpl.updatePubCarDriverInfo(businessPubCarDriverInfo);
//                if (businessPubCarDriverInfo.size() == 1) {
//                    dataFlowContext.addParamOut("violationId", businessPubCarDriverInfo.get("violationId"));
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
    public IPubCarRepairServiceDao getPubCarRepairServiceDaoImpl() {
        return pubCarRepairServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarViolation
     */
    private void doBusinessViolation(Business business, JSONObject businessPubCarViolation){

        Assert.jsonObjectHaveKey(businessPubCarViolation,"violationId","businessPubCarViolation 节点下没有包含 violationId 节点");

        if(businessPubCarViolation.getString("violationId").startsWith("-")){
            businessPubCarViolation.put("violationId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_DRIVER_ID));
        }

        businessPubCarViolation.put("bId",business.getbId());
        businessPubCarViolation.put("operate", StatusConstant.OPERATE_MOD);

        // 保存用车人或司机业务信息
//        pubCarDriverServiceDaoImpl.saveBusinessPubCarViolationInfo(businessPubCarViolation);

    }
}

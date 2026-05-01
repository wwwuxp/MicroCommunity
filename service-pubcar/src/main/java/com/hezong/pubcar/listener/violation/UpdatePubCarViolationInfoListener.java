package com.hezong.pubcar.listener.violation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarViolationServiceDao;
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
import java.util.List;
import java.util.Map;

/**
 * 修改违章信息
 * @author hh
 * @date 2021/8/5 15:49
 */
@Java110Listener("updatePubCarViolationInfoListener")
@Transactional
public class UpdatePubCarViolationInfoListener extends AbstractPubCarViolationBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarViolationServiceDao pubCarViolationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarViolationPo.class.getSimpleName())){
            Object obj = data.get(PubCarViolationPo.class.getSimpleName());
            JSONArray businessPubCarViolations;
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

        Map<String, Object> info = new HashMap<>();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);

       List<Map> businessPubCarViolationInfos = pubCarViolationServiceDaoImpl.getBusinessPubCarViolationInfo(info);
       if (businessPubCarViolationInfos != null && businessPubCarViolationInfos.size() > 0) {
           for (Map businessPubCarViolationInfo : businessPubCarViolationInfos) {

               flushBusinessPubCarViolationInfo(businessPubCarViolationInfo, StatusConstant.STATUS_CD_VALID);

               pubCarViolationServiceDaoImpl.updatePubCarViolationInfo(businessPubCarViolationInfo);
               if (businessPubCarViolationInfo.size() == 1) {
                   dataFlowContext.addParamOut("violationId", businessPubCarViolationInfo.get("violationId"));
               }
           }
       }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_VIOLATION;
    }

    @Override
    public IPubCarViolationServiceDao getPubCarViolationServiceDaoImpl() {
        return pubCarViolationServiceDaoImpl;
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

        // 保存违章业务信息
        pubCarViolationServiceDaoImpl.saveBusinessPubCarViolationInfo(businessPubCarViolation);

    }
}

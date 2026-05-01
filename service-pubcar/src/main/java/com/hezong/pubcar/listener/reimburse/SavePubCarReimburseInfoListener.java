package com.hezong.pubcar.listener.reimburse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarReimburseServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarReimburse.PubCarReimbursePo;
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
 * 保存用车报销 侦听
 * @author hh
 * @date 2021/10/9
 */
@Java110Listener("savePubCarReimburseInfoListener")
@Transactional
public class SavePubCarReimburseInfoListener extends AbstractPubCarReimburseBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarReimburseServiceDao pubCarReimburseServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarReimbursePo.class.getSimpleName())){
            Object obj = data.get(PubCarReimbursePo.class.getSimpleName());
            JSONArray businessPubCarReimburses;
            if(obj instanceof JSONObject){
                businessPubCarReimburses = new JSONArray();
                businessPubCarReimburses.add(obj);
            }else {
                businessPubCarReimburses = (JSONArray)obj;
            }
            for (int pubCarReimburseIndex = 0; pubCarReimburseIndex < businessPubCarReimburses.size();pubCarReimburseIndex++) {
                JSONObject businessPubCarReimburse = businessPubCarReimburses.getJSONObject(pubCarReimburseIndex);
                doBusinessReimburse(business, businessPubCarReimburse);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("reimburseId", businessPubCarReimburse.getString("reimburseId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map<String, Object> info = new HashMap<>();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 用车报销信息
        List<Map<String, Object>> businessPubCarReimburseInfo = pubCarReimburseServiceDaoImpl.getBusinessPubCarReimburseInfo(info);
        if(CollectionUtils.isNotEmpty(businessPubCarReimburseInfo)) {
            pubCarReimburseServiceDaoImpl.savePubCarReimburseInfo(info);
            if(businessPubCarReimburseInfo.size() == 1) {
                dataFlowContext.addParamOut("reimburseId", businessPubCarReimburseInfo.get(0).get("reimburseId"));
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
        // String bId = business.getbId();
        // Map<String, String> info = new HashMap<>();
        // info.put("bId", bId);
        // info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        // Map<String, String> paramIn = new HashMap<>();
        // paramIn.put("bId", bId);
        // paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        // // 异常报警信息
        // List<Map> pubCarAlarmInfo = pubCarAlarmServiceDaoImpl.getPubCarAlarmInstanceInfo(info);
        // if (CollectionUtils.isNotEmpty(pubCarAlarmInfo)) {
        //     pubCarAlarmServiceDaoImpl.updatePubCarAlarmInfo(paramIn);
        // }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_REIMBURSE;
    }

    @Override
    public IPubCarReimburseServiceDao getPubCarReimburseServiceDaoImpl() {
        return pubCarReimburseServiceDaoImpl;
    }

    /**
     * 处理 businessPubCarReimburse 节点
     * @param business
     * @param businessPubCarReimburse
     */
    private void doBusinessReimburse(Business business, JSONObject businessPubCarReimburse){

        Assert.jsonObjectHaveKey(businessPubCarReimburse,"reimburseId","businessPubCarReimburse 节点下没有包含 reimburseId 节点");

        if(businessPubCarReimburse.getString("reimburseId").startsWith("-")){
            businessPubCarReimburse.put("reimburseId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_REIMBURSE_ID));
        }

        businessPubCarReimburse.put("bId",business.getbId());
        businessPubCarReimburse.put("operate", StatusConstant.OPERATE_ADD);
        // 保存用车报销业务信息
        pubCarReimburseServiceDaoImpl.saveBusinessPubCarReimburseInfo(businessPubCarReimburse);

    }
}

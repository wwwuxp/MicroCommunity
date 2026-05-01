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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存车辆违章信息 侦听
 * @author hh
 * @datetime 2021/8/5 15:49
 */
@Java110Listener("savePubCarViolationInfoListener")
@Transactional
public class SavePubCarViolationInfoListener extends AbstractPubCarViolationBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarViolationServiceDao pubCarViolationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
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
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 车辆违章信息
        List<Map> businessPubCarViolationInfo = pubCarViolationServiceDaoImpl.getBusinessPubCarViolationInfo(info);
        if(businessPubCarViolationInfo != null && businessPubCarViolationInfo.size() >0) {
            pubCarViolationServiceDaoImpl.savePubCarViolationInfo(info);
            if(businessPubCarViolationInfo.size() == 1) {
                dataFlowContext.addParamOut("violationId", businessPubCarViolationInfo.get(0).get("violationId"));
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
        // 车辆违章信息
        List<Map> pubCarViolationInfo = pubCarViolationServiceDaoImpl.getPubCarViolationInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarViolationInfo)) {
            pubCarViolationServiceDaoImpl.updatePubCarViolationInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_VIOLATION;
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
            businessPubCarViolation.put("violationId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_VIOLATION_ID));
        }

        businessPubCarViolation.put("bId",business.getbId());
        businessPubCarViolation.put("operate", StatusConstant.OPERATE_ADD);
        // 保存车辆违章业务信息
        pubCarViolationServiceDaoImpl.saveBusinessPubCarViolationInfo(businessPubCarViolation);

    }
}

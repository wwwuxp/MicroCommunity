package com.hezong.pubcar.listener.accident;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarAccidentServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarAccident.PubCarAccidentPo;
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
 * 保存用车事故 侦听
 * @author hh
 * @date 2021/10/9
 */
@Java110Listener("savePubCarAccidentInfoListener")
@Transactional
public class SavePubCarAccidentInfoListener extends AbstractPubCarAccidentBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarAccidentServiceDao pubCarAccidentServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarAccidentPo.class.getSimpleName())){
            Object obj = data.get(PubCarAccidentPo.class.getSimpleName());
            JSONArray businessPubCarAccidents;
            if(obj instanceof JSONObject){
                businessPubCarAccidents = new JSONArray();
                businessPubCarAccidents.add(obj);
            }else {
                businessPubCarAccidents = (JSONArray)obj;
            }
            for (int pubCarAccidentIndex = 0; pubCarAccidentIndex < businessPubCarAccidents.size();pubCarAccidentIndex++) {
                JSONObject businessPubCarAccident = businessPubCarAccidents.getJSONObject(pubCarAccidentIndex);
                doBusinessAccident(business, businessPubCarAccident);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("accidentId", businessPubCarAccident.getString("accidentId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map<String, Object> info = new HashMap<>();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 用车事故信息
        List<Map<String, Object>> businessPubCarAccidentInfo = pubCarAccidentServiceDaoImpl.getBusinessPubCarAccidentInfo(info);
        if(CollectionUtils.isNotEmpty(businessPubCarAccidentInfo)) {
            pubCarAccidentServiceDaoImpl.savePubCarAccidentInfo(info);
            if(businessPubCarAccidentInfo.size() == 1) {
                dataFlowContext.addParamOut("accidentId", businessPubCarAccidentInfo.get(0).get("accidentId"));
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
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_ACCIDENT;
    }

    @Override
    public IPubCarAccidentServiceDao getPubCarAccidentServiceDaoImpl() {
        return pubCarAccidentServiceDaoImpl;
    }

    /**
     * 处理 businessPubCarAccident 节点
     * @param business
     * @param businessPubCarAccident
     */
    private void doBusinessAccident(Business business, JSONObject businessPubCarAccident){

        Assert.jsonObjectHaveKey(businessPubCarAccident,"accidentId","businessPubCarAccident 节点下没有包含 accidentId 节点");

        if(businessPubCarAccident.getString("accidentId").startsWith("-")){
            businessPubCarAccident.put("accidentId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ACCIDENT_ID));
        }

        businessPubCarAccident.put("bId",business.getbId());
        businessPubCarAccident.put("operate", StatusConstant.OPERATE_ADD);
        // 保存用车事故业务信息
        pubCarAccidentServiceDaoImpl.saveBusinessPubCarAccidentInfo(businessPubCarAccident);

    }
}

package com.hezong.pubcar.listener.approvalProcess;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarApprovalProcessServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarApprovalProcess.PubCarApprovalProcessPo;
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
 * 保存用车审批流程信息 侦听
 * @author hh
 * @datetime 2021/12/6 16:58
 */
@Java110Listener("savePubCarApprovalProcessInfoListener")
@Transactional
public class SavePubCarApprovalProcessInfoListener extends AbstractPubCarApprovalProcessBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarApprovalProcessServiceDao pubCarApprovalProcessServiceDaoImpl;

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarApprovalProcessPo.class.getSimpleName())){
            Object obj = data.get(PubCarApprovalProcessPo.class.getSimpleName());
            JSONArray businessPubCarApprovalProcesss;
            if(obj instanceof JSONObject){
                businessPubCarApprovalProcesss = new JSONArray();
                businessPubCarApprovalProcesss.add(obj);
            }else {
                businessPubCarApprovalProcesss = (JSONArray)obj;
            }
            for (int pubCarApprovalProcessIndex = 0; pubCarApprovalProcessIndex < businessPubCarApprovalProcesss.size();pubCarApprovalProcessIndex++) {
                JSONObject businessPubCarApprovalProcess = businessPubCarApprovalProcesss.getJSONObject(pubCarApprovalProcessIndex);
                doBusinessPubCarApprovalProcess(business, businessPubCarApprovalProcess);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("approvalProcessId", businessPubCarApprovalProcess.getString("approvalProcessId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 用车审批流程信息
        List<Map> businessPubCarTripInfo = pubCarApprovalProcessServiceDaoImpl.getBusinessPubCarApprovalProcessInfo(info);
        if( businessPubCarTripInfo != null && businessPubCarTripInfo.size() >0) {
            pubCarApprovalProcessServiceDaoImpl.savePubCarApprovalProcessInfo(info);
            if(businessPubCarTripInfo.size() == 1) {
                dataFlowContext.addParamOut("approvalProcessId", businessPubCarTripInfo.get(0).get("approvalProcessId"));
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
        // 用车审批流程信息
        List<Map> pubCarDriverInfo = pubCarApprovalProcessServiceDaoImpl.getPubCarApprovalProcessInstanceInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarDriverInfo)) {
            pubCarApprovalProcessServiceDaoImpl.updatePubCarApprovalProcessInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_APPROVAL_PROCESS;
    }

    @Override
    public IPubCarApprovalProcessServiceDao getPubCarApprovalProcessServiceDaoImpl() {
        return pubCarApprovalProcessServiceDaoImpl;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 处理 businessPubCarApprovalProcess 节点
     * @param business
     * @param businessPubCarApprovalProcess
     */
    private void doBusinessPubCarApprovalProcess(Business business, JSONObject businessPubCarApprovalProcess){

        Assert.jsonObjectHaveKey(businessPubCarApprovalProcess,"approvalProcessId","businessPubCarApprovalProcess 节点下没有包含 approvalProcessId 节点");

        if(businessPubCarApprovalProcess.getString("approvalProcessId").startsWith("-")){
            businessPubCarApprovalProcess.put("approvalProcessId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_APPROVAL_PROCESS_ID));
        }

        businessPubCarApprovalProcess.put("bId",business.getbId());
        businessPubCarApprovalProcess.put("operate", StatusConstant.OPERATE_ADD);
        // 保存业务信息
        pubCarApprovalProcessServiceDaoImpl.saveBusinessPubCarApprovalProcessInfo(businessPubCarApprovalProcess);

    }
}

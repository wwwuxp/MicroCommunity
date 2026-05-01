package com.hezong.pubcar.listener.approvalProcess;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarApprovalProcessServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.pubCarApprovalProcess.PubCarApprovalProcessPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除用车审批流程信息
 * @author hh
 * @datetime 2021/12/27 11:22
 */
@Java110Listener("deletePubCarApprovalProcessInfoListener")
@Transactional
public class DeletePubCarApprovalProcessInfoListener extends AbstractPubCarApprovalProcessBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarApprovalProcessServiceDao pubCarApprovalProcessServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

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
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        List<Map> businessPubCarApprovalProcessInfos = pubCarApprovalProcessServiceDaoImpl.getBusinessPubCarApprovalProcessInfo(info);
        if (businessPubCarApprovalProcessInfos != null && businessPubCarApprovalProcessInfos.size() > 0) {
            for (int pubCarApprovalProcessIndex = 0; pubCarApprovalProcessIndex < businessPubCarApprovalProcessInfos.size(); pubCarApprovalProcessIndex++) {

                Map businessPubCarApprovalProcessInfo = businessPubCarApprovalProcessInfos.get(pubCarApprovalProcessIndex);
                flushBusinessPubCarApprovalProcessInfo(businessPubCarApprovalProcessInfo, StatusConstant.STATUS_CD_INVALID);

                pubCarApprovalProcessServiceDaoImpl.updatePubCarApprovalProcessInfo(businessPubCarApprovalProcessInfo);
                if (businessPubCarApprovalProcessInfo.size() == 1) {
                    dataFlowContext.addParamOut("approvalProcessId", businessPubCarApprovalProcessInfo.get("approvalProcessId"));
                }
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
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map<String, String> delInfo = new HashMap<>();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        // 分组信息
        List<Map> pubCarApprovalProcessInfo = pubCarApprovalProcessServiceDaoImpl.getPubCarApprovalProcessInstanceInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarApprovalProcessInfo)) {

            // 分组信息
            List<Map> businessPubCarGroupInfos = pubCarApprovalProcessServiceDaoImpl.getBusinessPubCarApprovalProcessInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarGroupInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarApprovalProcess），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarGroupIndex = 0; pubCarGroupIndex < businessPubCarGroupInfos.size(); pubCarGroupIndex++) {
                Map businessPubCarGroupInfo = businessPubCarGroupInfos.get(pubCarGroupIndex);
                flushBusinessPubCarApprovalProcessInfo(businessPubCarGroupInfo, StatusConstant.STATUS_CD_VALID);
                pubCarApprovalProcessServiceDaoImpl.updatePubCarApprovalProcessInfo(businessPubCarGroupInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_APPROVAL_PROCESS;
    }

    @Override
    public IPubCarApprovalProcessServiceDao getPubCarApprovalProcessServiceDaoImpl() {
        return pubCarApprovalProcessServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarApprovalProcess
     */
    private void doBusinessPubCarApprovalProcess(Business business, JSONObject businessPubCarApprovalProcess){

        // Assert.jsonObjectHaveKey(businessPubCarApprovalProcess,"approvalProcessId","businessPubCarApprovalProcess 节点下没有包含 approvalProcessId 节点");
        //
        // if(businessPubCarApprovalProcess.getString("approvalProcessId").startsWith("-")){
        //     businessPubCarApprovalProcess.put("approvalProcessId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_APPROVAL_PROCESS_ID));
        // }

        autoSaveDelBusinessPubCarApprovalProcess(business, businessPubCarApprovalProcess);

    }
}

package com.java110.api.listener.pubCar.repairForm;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCar.pubcarRepairFrom.IPubcarRepairFromBMO;
import com.java110.api.bmo.pubCarApprovalProcess.IPubCarApprovalProcessBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
import com.java110.po.pubCarApprovalProcess.PubCarApprovalProcessPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.ServiceCodePubcarRepairFromConstant;
import com.java110.utils.constant.StatusConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

/**
 * 添加公车申请维修
 *
 * @author lmf
 * @since 2022-07-26 17:26:21
 */
@Java110Listener("savePubcarRepairFormListener")
public class SavePubcarRepairFormListener extends AbstractServiceApiPlusListener {

    @Autowired
    IPubcarRepairFromBMO pubcarRepairFromBMO;

    @Autowired
    IPubCarUserInnerServiceSMO pubCarUserInnerServiceSMO;

    @Autowired
    private IPubCarApprovalProcessBMO pubCarApprovalProcessBMOImpl;

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMO;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        String repairId = reqJson.getString("repairId");
        //判断新增还是修改
        boolean flag = StringUtils.isEmpty(repairId);
        String userId = event.getDataFlowContext().getUserId();
        if(!flag){
            //非修车审批人不得审批 2022-09-21 修改为其他的判断
            // String auditStatus = reqJson.getString("auditStatus") == null ? "" : reqJson.getString("auditStatus");
            // List<String> list = new ArrayList<>();
            // list.add("2");
            // list.add("3");
            // List<String> pubCarApprovers = pubCarApproverInnerServiceSMO.getPubCarApprovers(PubCarApproverPo.VEHICLE_REPAIR, PubCarApproverPo.TYPE_PUBCAR);
            // //如果状态发生改变，且为修车管理员，插入管理员id
            // if(list.contains(auditStatus) && pubCarApprovers.contains(userId)){
            //     reqJson.put("uId", userId);
            // }else if(list.contains(auditStatus) && !pubCarApprovers.contains(userId)){
            //     throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_ERROR, "请使用修车管理员审批");
            // }
            reqJson.put("uId", userId);
        }

        //传输数据进行处理
        pubcarRepairFromBMO.saveOrEditPubcarRepairFrom(reqJson, context);
        // 添加审批流程数据信息
        if(flag){
            repairId = reqJson.getString("repairId");
            pubCarApprovalProcessBMOImpl.addPubCarApprovalProcess(reqJson, context, repairId);
        }else{
            JSONObject approvalProcessStatusJson = new JSONObject();
            approvalProcessStatusJson.put("approvalStatus", StatusConstant.STATUS_FINISHED);
            approvalProcessStatusJson.put("tripId", reqJson.getString("tripId"));
            approvalProcessStatusJson.put("reviewerRole", PubCarApprovalProcessPo.VEHICLE_REPAIR);
            approvalProcessStatusJson.put("communityId", reqJson.getString("communityId"));
            pubCarApprovalProcessBMOImpl.updatePubCarApprovalProcess(approvalProcessStatusJson, context);
        }
        // 删除缓存中保存的数据
        String key = ServiceCodePubcarRepairFromConstant.PREFIX + userId;
        CommonCache.removeValue(key);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubcarRepairFromConstant.ADD_OR_MODIFY_MAINTENANCE_WORK_ORDER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}


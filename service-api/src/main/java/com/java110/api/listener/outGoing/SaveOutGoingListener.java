package com.java110.api.listener.outGoing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.PubCarTripCarPassenger.IPubCarTripCarPassengerBMO;
import com.java110.api.bmo.outGoing.IOutGoingBMO;
import com.java110.api.bmo.outGoingPersonnel.IOutGoingPersonnelBMO;
import com.java110.api.bmo.outGoingUnitContact.IOutGoingUnitContactBMO;
import com.java110.api.bmo.pubCarApprovalProcess.IPubCarApprovalProcessBMO;
import com.java110.api.bmo.pubCarTrip.IPubCarTripBMO;
import com.java110.api.bmo.pubCarTripCarNum.IPubCarTripCarNumBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.po.pubCarApprover.PubCarApproverPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeOutGoingConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.StringJoiner;

/**
 * 保存外出信息侦听
 * add by hh
 */
@Java110Listener("saveOutGoingListener")
public class SaveOutGoingListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IOutGoingBMO outGoingBMOImpl;

    @Autowired
    private IOutGoingPersonnelBMO outGoingPersonnelBMOImpl;

    @Autowired
    private IOutGoingUnitContactBMO outGoingUnitContactBMOImpl;

    @Autowired
    private IPubCarTripBMO pubCarTripBMOImpl;

    @Autowired
    private IPubCarTripCarNumBMO pubCarTripCarNumBMOImpl;

    @Autowired
    private IPubCarTripCarPassengerBMO pubCarTripCarPassengerBMOImpl;

    @Autowired
    private IPubCarApprovalProcessBMO pubCarApprovalProcessBMOImpl;

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "outType", "外出类型不能为空");
        Assert.hasKeyAndValue(reqJson, "destination", "目的地不能为空");
        Assert.hasKeyAndValue(reqJson, "startTime", "开始时间不能为空");
        Assert.hasKeyAndValue(reqJson, "endTime", "预计结束时间不能为空");
        Assert.hasKeyAndValue(reqJson, "handlingMatter", "经办事项不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id不能为空");
        Assert.hasKey(reqJson, "personnel", "请重新进入此菜单");
        if (CollectionUtils.isEmpty(reqJson.getJSONArray("personnel"))){
            throw new IllegalArgumentException("外出人员不能为空");
        }
        Assert.hasKey(reqJson, "contact", "请重新进入此菜单");
        JSONArray contactArray = reqJson.getJSONArray("contact");
        if (CollectionUtils.isEmpty(contactArray)){
            throw new IllegalArgumentException("外出单位联系人不能为空");
        }
        for (int i = 0; i < contactArray.size(); i++) {
            JSONObject oneContactJson = contactArray.getJSONObject(i);
            Assert.hasKeyAndValue(oneContactJson, "name", "存在外出单位联系人姓名为空");
            Assert.hasKeyAndValue(oneContactJson, "tel", "存在外出单位联系人电话为空");
        }

        Assert.hasKey(reqJson, "carApplication", "请重新进入此菜单");
        JSONObject carApplicationJson = reqJson.getJSONObject("carApplication");
        if (carApplicationJson.containsKey("trip")){
            JSONArray tripArray = carApplicationJson.getJSONArray("trip");
            if (CollectionUtils.isEmpty(tripArray)){
                return;
            }

            carApplicationJson.put("communityId", reqJson.getString("communityId"));
            carApplicationJson.put("userId", event.getDataFlowContext().getUserId());

            // 处理是否加急，是否是日常用车等信息
            if (!carApplicationJson.containsKey("isUrgent")){
                carApplicationJson.put("isUrgent", 0);
            }
            if (!carApplicationJson.containsKey("isDaily")){
                carApplicationJson.put("isDaily", 0);
            }
            if (!carApplicationJson.containsKey("dailyPeriod")){
                carApplicationJson.put("dailyPeriod", 0);
            }

            // 校验填写的申请用车信息
            commonTripValidate(tripArray);

        }

    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        String tripId = "0";
        JSONObject carApplicationJson = reqJson.getJSONObject("carApplication");

        if (carApplicationJson.containsKey("trip") && CollectionUtils.isNotEmpty(carApplicationJson.getJSONArray("trip"))){
            // 添加申请用车基本信息
            pubCarTripBMOImpl.addPubCarTrip(carApplicationJson, context);

            JSONArray tripArray = carApplicationJson.getJSONArray("trip");
            StringJoiner sj = new StringJoiner(",");
            for (int i = 0; i < tripArray.size(); i++) {
                JSONObject oneTripJson = tripArray.getJSONObject(i);
                sj.add(oneTripJson.getString("tripId"));
            }
            tripId = sj.toString();

            // 添加申请车辆数信息
            pubCarTripCarNumBMOImpl.addPubCarTripCarNum(carApplicationJson, context);

            // 添加乘车人信息
            pubCarTripCarPassengerBMOImpl.addPubCarTripCarPassenger(carApplicationJson, context);

            // 找到申请用车人的上级领导
            List<String> leaderIdList = pubCarApproverInnerServiceSMOImpl.getUserLeader(context.getUserId(), PubCarApproverPo.TYPE_PUBCAR);
            if (CollectionUtils.isEmpty(leaderIdList)){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, "请先联系管理员配置各部门领导");
            }
            carApplicationJson.put("userId", context.getUserId());
            pubCarApprovalProcessBMOImpl.addPubCarApprovalProcess(carApplicationJson, context, leaderIdList, false);
        }

        // 保存外出信息
        reqJson.put("tripId", tripId);
        outGoingBMOImpl.addOutGoing(reqJson, context);

        outGoingPersonnelBMOImpl.addOutGoingPersonnel(reqJson, context);

        outGoingUnitContactBMOImpl.addOutGoingUnitContact(reqJson, context);

        // 添加外出审批流程信息
        pubCarApprovalProcessBMOImpl.addOutGoingApprovalProcess(reqJson, context);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodeOutGoingConstant.ADD_OUTGOING;
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

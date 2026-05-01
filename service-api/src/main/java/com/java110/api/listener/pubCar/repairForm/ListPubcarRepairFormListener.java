package com.java110.api.listener.pubCar.repairForm;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormDto;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormWhereDto;
import com.java110.entity.pubcar.PubcarRepairFormFlow;
import com.java110.intf.pubcar.PubcarRepairFormFlowSMO;
import com.java110.intf.pubcar.PubcarRepairFormSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubcarRepairFromConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 添加公车申请维修
 *
 * @author lmf
 * @since 2022-07-26 17:26:21
 */
@Java110Listener("listPubcarRepairFormListener")
public class ListPubcarRepairFormListener extends AbstractServiceApiPlusListener {

    @Autowired
    PubcarRepairFormSMO pubcarRepairFormSMO;

    @Autowired
    PubcarRepairFormFlowSMO pubcarRepairFormFlowSMO;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "row", "（row）显示行未填");
        Assert.jsonObjectHaveKey(reqJson, "page", "（page）页未填");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        PubcarRepairFormWhereDto param = reqJson.toJavaObject(PubcarRepairFormWhereDto.class);
        param.setDataPage();
        //这里判断是否是管理员账号
        String userId = context.getUserId();
        boolean isAdministrator = userInnerServiceSMOImpl.isAdministrator(userId);
        if(!isAdministrator && "1".equals(param.getAuditStatus())){
            // 如果是管理员查看所有的，但是还是不能审批，他只能看
            param.setUserId(userId);
        }
        int count = pubcarRepairFormSMO.quertyPageCount(param);
        List<PubcarRepairFormDto> pubcarRepairForm;
        if (count > 0) {
            pubcarRepairForm = pubcarRepairFormSMO.quertyPageList(param);
            //获取审批过程
            List<String> ids = pubcarRepairForm.stream().map(PubcarRepairFormDto::getRepairId).collect(Collectors.toList());
            List<PubcarRepairFormFlow> prffList = pubcarRepairFormFlowSMO.queryListByIds(ids);
            Map<String, List<PubcarRepairFormFlow>> prffMap = prffList.stream().collect(Collectors.groupingBy(PubcarRepairFormFlow::getRepairId));
            pubcarRepairForm.forEach(r->{
                r.setRepairFormFlowList(prffMap.get(r.getRepairId()));
            });
        } else {
            pubcarRepairForm = Collections.emptyList();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", count);
        jsonObject.put("row", param.getRow());
        jsonObject.put("page", param.getPage());
        jsonObject.put("records", (int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        jsonObject.put("data", pubcarRepairForm);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonObject.toJSONString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubcarRepairFromConstant.LIST_MAINTENANCE_WORK_ORDER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}


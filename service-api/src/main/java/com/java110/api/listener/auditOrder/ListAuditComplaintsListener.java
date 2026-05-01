package com.java110.api.listener.auditOrder;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.entity.audit.AuditUser;
import com.java110.intf.common.IComplaintUserInnerServiceSMO;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodeAuditUserConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.complaint.ApiComplaintDataVo;
import com.java110.vo.api.complaint.ApiComplaintVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询审核订单侦听类
 */
@Java110Listener("listAuditComplaintsListener")
public class ListAuditComplaintsListener extends AbstractServiceApiListener {

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodeAuditUserConstant.LIST_AUDIT_COMPLAINTS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public IComplaintUserInnerServiceSMO getComplaintUserInnerServiceSMOImpl() {
        return complaintUserInnerServiceSMOImpl;
    }

    public void setComplaintUserInnerServiceSMOImpl(IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl) {
        this.complaintUserInnerServiceSMOImpl = complaintUserInnerServiceSMOImpl;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "storeId", "必填，请填写商户ID");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写用户ID");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");

        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        AuditUser auditUser = new AuditUser();
        auditUser.setUserId(reqJson.getString("userId"));
        auditUser.setStoreId(reqJson.getString("storeId"));
        auditUser.setCommunityId(reqJson.getString("communityId"));
        auditUser.setPage(reqJson.getInteger("page"));
        auditUser.setRow(reqJson.getInteger("row"));


        long count = 0;
        List<ApiComplaintDataVo> auditComplaints = null;
        try {
            count = complaintUserInnerServiceSMOImpl.getUserTaskCount(auditUser);

            if (count > 0) {
                auditComplaints = BeanConvertUtil.covertBeanList(complaintUserInnerServiceSMOImpl.getUserTasks(auditUser), ApiComplaintDataVo.class);
            } else {
                auditComplaints = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            if (StringUtils.isNotBlank(message) && message.startsWith("请求参数错误") && message.contains("{") && message.contains("}")){
                JSONObject bodyJson = JSONObject.parseObject(message.substring(message.indexOf("{"), message.indexOf("}") + 1));
                String newMessage = bodyJson.getString("message");
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_CONFIG_ERROR, newMessage);
            }
            throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, message);
        }

        ApiComplaintVo apiComplaintVo = new ApiComplaintVo();

        apiComplaintVo.setTotal((int) count);
        apiComplaintVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiComplaintVo.setComplaints(auditComplaints);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiComplaintVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }
}

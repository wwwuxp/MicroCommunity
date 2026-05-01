package com.java110.api.listener.visitInvitation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.visitInvitation.IVisitInvitationBMO;
import com.java110.api.bmo.visitor.IVisitorBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.po.visitInvitation.VisitInvitationPo;
import com.java110.utils.constant.ServiceCodeVisitInvitationConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ConstantUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

/**
 * 保存访客邀约信息 服务侦听
 * @author hh
 * create date 2021/9/16
 */
@Java110Listener("saveVisitInvitationListener")
public class SaveVisitInvitationListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IVisitInvitationBMO visitInvitationBMOImpl;

    @Autowired
    private IVisitorBMO visitorBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "visitType", "到访类型不能为空");
        if (VisitInvitationPo.CUSTOM.equals(reqJson.getInteger("visitType"))){
            Assert.hasKeyAndValue(reqJson, "visitTime", "到访时间不能为空");
        }
        Assert.hasKey(reqJson, "visitors", "visitors节点不能为空");
        JSONArray visitorArray = reqJson.getJSONArray("visitors");
        if (CollectionUtils.isEmpty(visitorArray)){
            throw new IllegalArgumentException("访客信息不能为空");
        }
        for (int i = 0; i < visitorArray.size(); i++) {
            JSONObject visitorObject = visitorArray.getJSONObject(i);
            Assert.hasKeyAndValue(visitorObject, "no", "到访人编号不能为空");
            Assert.hasKeyAndValue(visitorObject, "name", "到访人姓名不能为空");
            String tel = visitorObject.getString("tel");
            if (StringUtils.isNotBlank(tel)){
                // 校验电话号码格式
                if (!tel.matches(ConstantUtil.REGEX_TEL)){
                    throw new IllegalArgumentException("非法电话号码格式");
                }
            }
            String idCard = visitorObject.getString("idCard");
            if (StringUtils.isNotBlank(idCard) && !idCard.matches(ConstantUtil.REGEX_ID_NUM)){
                throw new IllegalArgumentException("非法身份证号码格式");
            }
            String license = visitorObject.getString("license");
            if (StringUtils.isNotBlank(license) && license.length() > 10){
                throw new IllegalArgumentException("车牌号最多允许录入10个字符");
            }
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        visitInvitationBMOImpl.addVisitInvitation(reqJson, context);

        visitorBMOImpl.addVisitor(reqJson, context);
    }

    @Override
    public void afterHandle(DataFlowContext dataFlowContext, JSONObject reqJson) {
        ResponseEntity responseEntity = dataFlowContext.getResponseEntity();
        JSONObject body = JSONObject.parseObject((String) responseEntity.getBody());
        JSONArray data = new JSONArray();
        JSONObject codeJson = new JSONObject();
        codeJson.put("qrCode", reqJson.getString("qrCode"));
        data.add(codeJson);
        body.put("data", data);
        ResponseEntity<String> newResponseEntity = new ResponseEntity<>(body.toJSONString(), HttpStatus.OK);
        dataFlowContext.setResponseEntity(newResponseEntity);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodeVisitInvitationConstant.ADD_VISIT_INVITATION;
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

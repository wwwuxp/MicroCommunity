package com.java110.api.bmo.visitInvitation.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.visitInvitation.IVisitInvitationBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.community.IVisitInvitationInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.visitInvitation.VisitInvitationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.AESUtil;
import com.java110.utils.util.DateUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author hh
 * create date 2021/9/16
 */
@Service("visitInvitationBMOImpl")
public class VisitInvitationBMOImpl extends ApiBaseBMO implements IVisitInvitationBMO {

    @Autowired
    private IVisitInvitationInnerServiceSMO visitInvitationInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void addVisitInvitation(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessVisitInvitation = getBusinessVisitInvitationJsonObject(reqJson, context);
        handleBusinessJsonObjectData(context, businessVisitInvitation, BusinessTypeConstant.BUSINESS_TYPE_SAVE_VISIT_INVITATION, VisitInvitationPo.class);
    }

    @Override
    public void updateVisitInvitation(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessVisitInvitation = getBusinessVisitInvitationJsonObject(reqJson, context);
        handleBusinessJsonObjectData(context, businessVisitInvitation, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_VISIT_INVITATION, VisitInvitationPo.class);
    }

    @Override
    public void deleteVisitInvitation(JSONObject reqJson, DataFlowContext context) {
        handleBusinessJsonObjectData(context, reqJson, BusinessTypeConstant.BUSINESS_TYPE_DELETE_VISIT_INVITATION, VisitInvitationPo.class);
    }

    /**
     * 生成业务json
     * @param reqJson 请求json
     * @param context 上下文
     * @return JSONObject
     */
    private JSONObject getBusinessVisitInvitationJsonObject(JSONObject reqJson, DataFlowContext context) {
        JSONObject businessVisitInvitation = new JSONObject();

        if (!reqJson.containsKey("invitationId")) {
            String invitationId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_VISIT_INVITATION_ID);
            businessVisitInvitation.put("invitationId", invitationId);
            reqJson.put("invitationId", invitationId);
            businessVisitInvitation.put("isCome", 0);
        }else {
            businessVisitInvitation.put("invitationId", reqJson.getString("invitationId"));
            businessVisitInvitation.put("isCome", reqJson.getInteger("isCome"));
        }

        Integer visitType = reqJson.getInteger("visitType");
        businessVisitInvitation.put("visitType", visitType);

        if (VisitInvitationPo.CUSTOM.equals(visitType)){
            businessVisitInvitation.put("visitTime", reqJson.getString("visitTime"));
        } else {
            putVisitTime(businessVisitInvitation, visitType);
        }

        // 生成8位的随机访客id，用于生成访客二维码
        String uniqueCode;
        // int num = 0;
        do {
            // 随机5次，如果依然有重复的，采用数字加字母组合
            // if (num >= 5){
            //     uniqueCode = RandomStringUtils.randomAlphanumeric(8);
            // }else {
                uniqueCode = RandomStringUtils.randomNumeric(8);
            // }
            // num++;
        } while (visitInvitationInnerServiceSMOImpl.hasSameUniqueCode(uniqueCode, reqJson.getString("communityId")));

        businessVisitInvitation.put("uniqueCode", uniqueCode);
        reqJson.put("uniqueCode", uniqueCode);

        businessVisitInvitation.put("userId", context.getUserId());
        businessVisitInvitation.put("communityId", reqJson.getString("communityId"));

        String qrCode = generateQrCode(context, reqJson);
        businessVisitInvitation.put("qrCode", qrCode);
        businessVisitInvitation.put("invitationReason", reqJson.getString("invitationReason"));
        reqJson.put("qrCode", qrCode);
        return businessVisitInvitation;
    }

    /**
     * 当访问时间类型是今天、明天和后天时，在startTime字段上放对应的时间
     * @param businessVisitInvitation businessVisitInvitation
     * @param visitType visitType
     */
    private void putVisitTime(JSONObject businessVisitInvitation, Integer visitType) {
        String visitTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B);
        LocalDate now = LocalDate.now();
        switch (visitType){
            case 1:
                visitTime = now.format(formatter);
                break;
            case 2:
                visitTime = now.plusDays(1).format(formatter);
                break;
            case 3:
                visitTime = now.plusDays(2).format(formatter);
                break;
            default:
                visitTime = null;
        }
        businessVisitInvitation.put("visitTime", visitTime);
    }


    /**
     * 生成访客邀约二维码方法
     * @param dataFlowContext dataFlowContext
     * @param reqJson reqJson
     */
    private String generateQrCode(DataFlowContext dataFlowContext, JSONObject reqJson) {
        // 6位邀约人id
        String invitorId = userInnerServiceSMOImpl.getInvitorIdByUserId(dataFlowContext.getUserId());
        // 8位访客id
        String uniqueCode = reqJson.getString("uniqueCode");
        // 3位的访客人数数字
        int size = reqJson.getJSONArray("visitors").size();
        String sizeStr = "000";
        switch (String.valueOf(size).length()){
            case 1: sizeStr = "00" + size;break;
            case 2: sizeStr = "0" + size;break;
            case 3: sizeStr = String.valueOf(size);break;
            default:
        }

        // 14位的二维码有效时间
        Integer visitType = reqJson.getInteger("visitType");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_H);
        LocalDateTime now = LocalDateTime.now();

        String effectiveTime;
        switch (visitType){
            case 1:
                effectiveTime = now.format(formatter);
                break;
            case 2:
                effectiveTime = now.plusDays(1).format(formatter);
                break;
            case 3:
                effectiveTime = now.plusDays(2).format(formatter);
                break;
            default:
                Instant instant = reqJson.getDate("visitTime").toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime customDateTime = instant.atZone(zoneId).toLocalDateTime();
                effectiveTime = customDateTime.format(formatter);
        }
        // 明文
        String plainText = CommonConstant.PREFIX_QR_CODE + invitorId + uniqueCode + sizeStr + effectiveTime + System.currentTimeMillis();
        return AESUtil.encrypt(plainText);
    }

}

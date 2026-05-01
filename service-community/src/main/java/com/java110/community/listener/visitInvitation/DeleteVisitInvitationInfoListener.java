package com.java110.community.listener.visitInvitation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitInvitationServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.visitInvitation.VisitInvitationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hh
 * create date 2021/9/23
 */
@Java110Listener("deleteVisitInvitationInfoListener")
@Transactional
public class DeleteVisitInvitationInfoListener extends AbstractVisitInvitationBusinessServiceDataFlowListener {

    @Autowired
    private IVisitInvitationServiceDao visitInvitationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(VisitInvitationPo.class.getSimpleName())){
            Object obj = data.get(VisitInvitationPo.class.getSimpleName());
            JSONArray businessVisitInvitations;
            if(obj instanceof JSONObject){
                businessVisitInvitations = new JSONArray();
                businessVisitInvitations.add(obj);
            }else {
                businessVisitInvitations = (JSONArray)obj;
            }
            for (int visitInvitationIndex = 0; visitInvitationIndex < businessVisitInvitations.size();visitInvitationIndex++) {
                JSONObject businessVisitInvitation = businessVisitInvitations.getJSONObject(visitInvitationIndex);
                doBusinessVisitInvitation(business, businessVisitInvitation);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("invitationId", businessVisitInvitation.getString("invitationId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        Map<String, String> info = new HashMap<>();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        List<Map<String, Object>> businessVisitorInfos = visitInvitationServiceDaoImpl.getBusinessVisitInvitationInfo(info);
        if (businessVisitorInfos != null && businessVisitorInfos.size() > 0) {
            for (Map<String, Object> businessVisitorInfo : businessVisitorInfos) {
                flushBusinessVisitInvitationInfo(businessVisitorInfo, StatusConstant.STATUS_CD_INVALID);

                visitInvitationServiceDaoImpl.updateVisitInvitationInfo(businessVisitorInfo);
                if (businessVisitorInfo.size() == 1) {
                    dataFlowContext.addParamOut("invitationId", businessVisitorInfo.get("invitationId"));
                }
            }
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_VISIT_INVITATION;
    }

    @Override
    public IVisitInvitationServiceDao getVisitInvitationServiceDaoImpl() {
        return visitInvitationServiceDaoImpl;
    }

    private void doBusinessVisitInvitation(Business business, JSONObject businessVisitInvitation) {
        Assert.jsonObjectHaveKey(businessVisitInvitation,"invitationId","businessVisitInvitation 节点下没有包含 invitationId 节点");

        if(businessVisitInvitation.getString("invitationId").startsWith("-")){
            businessVisitInvitation.put("invitationId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_VISIT_INVITATION_ID));
        }

        autoSaveDelBusinessVisitInvitation(business, businessVisitInvitation);

    }
}

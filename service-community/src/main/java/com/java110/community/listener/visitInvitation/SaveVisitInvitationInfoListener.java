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
 * 保存访客邀约信息
 *
 * @author hh
 * create date 2021/9/16
 */
@Java110Listener("saveVisitInvitationInfoListener")
@Transactional
public class SaveVisitInvitationInfoListener extends AbstractVisitInvitationBusinessServiceDataFlowListener {

    @Autowired
    private IVisitInvitationServiceDao visitInvitationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_VISIT_INVITATION;
    }

    @Override
    public IVisitInvitationServiceDao getVisitInvitationServiceDaoImpl() {
        return visitInvitationServiceDaoImpl;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        if (data.containsKey(VisitInvitationPo.class.getSimpleName())) {
            Object bObj = data.get(VisitInvitationPo.class.getSimpleName());
            JSONArray businessVisitInvitations;
            if (bObj instanceof JSONObject) {
                businessVisitInvitations = new JSONArray();
                businessVisitInvitations.add(bObj);
            } else {
                businessVisitInvitations = (JSONArray) bObj;
            }
            for (int visitInvitationIndex = 0; visitInvitationIndex < businessVisitInvitations.size(); visitInvitationIndex++) {
                JSONObject businessVisitInvitation = businessVisitInvitations.getJSONObject(visitInvitationIndex);
                doBusinessVisitInvitation(business, businessVisitInvitation);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("invitationId", businessVisitInvitation.getString("invitationId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map<String, String> info = new HashMap<>();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        // 访客邀约信息
        List<Map<String, Object>> businessVisitInvitationInfo = visitInvitationServiceDaoImpl.getBusinessVisitInvitationInfo(info);
        if (businessVisitInvitationInfo != null && businessVisitInvitationInfo.size() > 0) {
            visitInvitationServiceDaoImpl.saveVisitInvitationInfo(info);
            if (businessVisitInvitationInfo.size() == 1) {
                dataFlowContext.addParamOut("invitationId", businessVisitInvitationInfo.get(0).get("invitationId"));
            }
        }
    }

    /**
     * 撤单
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        // String bId = business.getbId();
        // //Assert.hasLength(bId,"请求报文中没有包含 bId");
        // Map info = new HashMap();
        // info.put("bId",bId);
        // info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        // Map paramIn = new HashMap();
        // paramIn.put("bId",bId);
        // paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        // //访客信息信息
        // List<Map> visitInfo = visitInvitationServiceDaoImpl.getVisitInfo(info);
        // if(visitInfo != null && visitInfo.size() > 0){
        //     reFreshShareColumn(paramIn, visitInfo.get(0));
        //     visitInvitationServiceDaoImpl.updateVisitInfoInstance(paramIn);
        // }
    }


    /**
     * 处理 businessVisit 节点
     *
     * @param business        总的数据节点
     * @param visitInvitation 访客信息节点
     */
    private void doBusinessVisitInvitation(Business business, JSONObject visitInvitation) {

        Assert.jsonObjectHaveKey(visitInvitation, "invitationId", "businessVisitInvitation 节点下没有包含 invitationId 节点");

        if (visitInvitation.getString("invitationId").startsWith("-")) {
            visitInvitation.put("invitationId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_VISIT_INVITATION_ID));
        }

        visitInvitation.put("bId", business.getbId());
        visitInvitation.put("operate", StatusConstant.OPERATE_ADD);
        //保存访客邀约信息
        visitInvitationServiceDaoImpl.saveBusinessVisitInvitationInfo(visitInvitation);

    }

}

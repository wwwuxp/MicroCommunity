package com.java110.community.listener.visitInvitation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitInvitationServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.visitInvitation.VisitInvitationPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hh
 * create date 2021/9/16
 */
@Java110Listener("updateVisitInvitationInfoListener")
@Transactional
public class UpdateVisitInvitationInfoListener extends AbstractVisitInvitationBusinessServiceDataFlowListener {

    @Autowired
    private IVisitInvitationServiceDao visitInvitationServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_VISIT_INVITATION;
    }

    @Override
    public IVisitInvitationServiceDao getVisitInvitationServiceDaoImpl() {
        return visitInvitationServiceDaoImpl;
    }

    /**
     * business过程
     *
     * @param dataFlowContext 上下文对象
     * @param business        业务对象
     */
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


    /**
     * business to instance 过程
     *
     * @param dataFlowContext 数据对象
     * @param business        当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map<String, String> info = new HashMap<>();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);

        // 访客邀约信息
        List<Map<String, Object>> businessVisitInvitationInfos = visitInvitationServiceDaoImpl.getBusinessVisitInvitationInfo(info);
        if (businessVisitInvitationInfos != null && businessVisitInvitationInfos.size() > 0) {
            for (Map<String, Object> businessVisitInvitationInfo : businessVisitInvitationInfos) {
                flushBusinessVisitInvitationInfo(businessVisitInvitationInfo, StatusConstant.STATUS_CD_VALID);
                visitInvitationServiceDaoImpl.updateVisitInvitationInfo(businessVisitInvitationInfo);
                if (businessVisitInvitationInfo.size() == 1) {
                    dataFlowContext.addParamOut("invitationId", businessVisitInvitationInfo.get("invitationId"));
                }
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
        // info.put("bId", bId);
        // info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        // Map delInfo = new HashMap();
        // delInfo.put("bId", business.getbId());
        // delInfo.put("operate", StatusConstant.OPERATE_DEL);
        // //访客信息信息
        // List<Map> visitInfo = visitInvitationServiceDaoImpl.getVisitInfo(info);
        // if (visitInfo != null && visitInfo.size() > 0) {
        //
        //     //访客信息信息
        //     List<Map> businessVisitInfos = visitInvitationServiceDaoImpl.getBusinessVisitInfo(delInfo);
        //     //除非程序出错了，这里不会为空
        //     if (businessVisitInfos == null || businessVisitInfos.size() == 0) {
        //         throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（visit），程序内部异常,请检查！ " + delInfo);
        //     }
        //     for (int _visitIndex = 0; _visitIndex < businessVisitInfos.size(); _visitIndex++) {
        //         Map businessVisitInfo = businessVisitInfos.get(_visitIndex);
        //         flushBusinessVisitInfo(businessVisitInfo, StatusConstant.STATUS_CD_VALID);
        //         visitInvitationServiceDaoImpl.updateVisitInfoInstance(businessVisitInfo);
        //     }
        // }

    }


    /**
     * 处理 businessVisit 节点
     *
     * @param business      总的数据节点
     * @param businessVisitInvitation 访客信息节点
     */
    private void doBusinessVisitInvitation(Business business, JSONObject businessVisitInvitation) {

        Assert.jsonObjectHaveKey(businessVisitInvitation, "invitationId", "businessVisitInvitation 节点下没有包含 invitationId 节点");

        if (businessVisitInvitation.getString("invitationId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "invitationId 错误，不能自动生成（必须已经存在的invitationId）" + businessVisitInvitation);
        }

        businessVisitInvitation.put("bId", business.getbId());
        businessVisitInvitation.put("operate", StatusConstant.OPERATE_MOD);
        // 保存访客邀约业务信息
        visitInvitationServiceDaoImpl.saveBusinessVisitInvitationInfo(businessVisitInvitation);

    }


}

package com.java110.community.listener.visitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitorServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.visitor.VisitorPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存访客邀约人信息
 *
 * @author hh
 * create date 2021/9/16
 */
@Java110Listener("saveVisitorInfoListener")
@Transactional
public class SaveVisitorInfoListener extends AbstractVisitorBusinessServiceDataFlowListener {

    @Autowired
    private IVisitorServiceDao visitorServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_VISITOR;
    }

    @Override
    public IVisitorServiceDao getVisitorServiceDaoImpl() {
        return visitorServiceDaoImpl;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        if (data.containsKey(VisitorPo.class.getSimpleName())) {
            Object bObj = data.get(VisitorPo.class.getSimpleName());
            JSONArray businessVisitors;
            if (bObj instanceof JSONObject) {
                businessVisitors = new JSONArray();
                businessVisitors.add(bObj);
            } else {
                businessVisitors = (JSONArray) bObj;
            }
            for (int visitorIndex = 0; visitorIndex < businessVisitors.size(); visitorIndex++) {
                JSONObject businessVisitor = businessVisitors.getJSONObject(visitorIndex);
                doBusinessVisitor(business, businessVisitor);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("visitorId", businessVisitor.getString("visitorId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map<String, String> info = new HashMap<>();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        // 访客邀约人信息
        List<Map<String, Object>> businessVisitorInfo = visitorServiceDaoImpl.getBusinessVisitorInfo(info);
        if (businessVisitorInfo != null && businessVisitorInfo.size() > 0) {
            visitorServiceDaoImpl.saveVisitorInfo(info);
            if (businessVisitorInfo.size() == 1) {
                dataFlowContext.addParamOut("visitorId", businessVisitorInfo.get(0).get("visitorId"));
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
     * @param visitor 访客信息节点
     */
    private void doBusinessVisitor(Business business, JSONObject visitor) {

        Assert.jsonObjectHaveKey(visitor, "visitorId", "businessVisitor 节点下没有包含 visitorId 节点");

        if (visitor.getString("visitorId").startsWith("-")) {
            visitor.put("visitorId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_VISITOR_ID));
        }

        visitor.put("bId", business.getbId());
        visitor.put("operate", StatusConstant.OPERATE_ADD);
        // 保存访客人邀约信息
        visitorServiceDaoImpl.saveBusinessVisitorInfo(visitor);

    }

}

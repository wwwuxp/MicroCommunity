package com.java110.community.listener.visitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitorServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.visitor.VisitorPo;
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
@Java110Listener("updateVisitorInfoListener")
@Transactional
public class UpdateVisitorInfoListener extends AbstractVisitorBusinessServiceDataFlowListener {

    @Autowired
    private IVisitorServiceDao visitorServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_VISITOR;
    }

    @Override
    public IVisitorServiceDao getVisitorServiceDaoImpl() {
        return visitorServiceDaoImpl;
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

        // 访客信息
        List<Map<String, Object>> businessVisitorInfos = visitorServiceDaoImpl.getBusinessVisitorInfo(info);
        if (businessVisitorInfos != null && businessVisitorInfos.size() > 0) {
            for (Map<String, Object> businessVisitorInfo : businessVisitorInfos) {
                flushBusinessVisitorInfo(businessVisitorInfo, StatusConstant.STATUS_CD_VALID);
                visitorServiceDaoImpl.updateVisitorInfo(businessVisitorInfo);
                if (businessVisitorInfo.size() == 1) {
                    dataFlowContext.addParamOut("visitorId", businessVisitorInfo.get("visitorId"));
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
     * @param businessVisitor 访客信息节点
     */
    private void doBusinessVisitor(Business business, JSONObject businessVisitor) {

        Assert.jsonObjectHaveKey(businessVisitor, "visitorId", "businessVisitor 节点下没有包含 visitorId 节点");

        if (businessVisitor.getString("visitorId").startsWith("-")) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "visitorId 错误，不能自动生成（必须已经存在的visitorId）" + businessVisitor);
        }

        businessVisitor.put("bId", business.getbId());
        businessVisitor.put("operate", StatusConstant.OPERATE_MOD);
        // 保存访客业务信息
        visitorServiceDaoImpl.saveBusinessVisitorInfo(businessVisitor);

    }


}

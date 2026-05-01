package com.java110.community.listener.visitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitorServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
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
 * @author hh
 * create date 2021/9/23
 */
@Java110Listener("deleteVisitorInfoListener")
@Transactional
public class DeleteVisitorInfoListener extends AbstractVisitorBusinessServiceDataFlowListener{

    @Autowired
    private IVisitorServiceDao visitorServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(VisitorPo.class.getSimpleName())){
            Object obj = data.get(VisitorPo.class.getSimpleName());
            JSONArray businessVisitors;
            if(obj instanceof JSONObject){
                businessVisitors = new JSONArray();
                businessVisitors.add(obj);
            }else {
                businessVisitors = (JSONArray)obj;
            }
            for (int visitorIndex = 0; visitorIndex < businessVisitors.size();visitorIndex++) {
                JSONObject businessVisitor = businessVisitors.getJSONObject(visitorIndex);
                doBusinessVisitor(business, businessVisitor);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("visitorId", businessVisitor.getString("visitorId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        Map<String, String> info = new HashMap<>();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        List<Map<String, Object>> businessVisitorInfos = visitorServiceDaoImpl.getBusinessVisitorInfo(info);
        if (businessVisitorInfos != null && businessVisitorInfos.size() > 0) {
            for (Map<String, Object> businessVisitorInfo : businessVisitorInfos) {
                flushBusinessVisitorInfo(businessVisitorInfo, StatusConstant.STATUS_CD_INVALID);

                visitorServiceDaoImpl.updateVisitorInfo(businessVisitorInfo);
                if (businessVisitorInfo.size() == 1) {
                    dataFlowContext.addParamOut("visitorId", businessVisitorInfo.get("visitorId"));
                }
            }
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_VISITOR;
    }

    @Override
    public IVisitorServiceDao getVisitorServiceDaoImpl() {
        return visitorServiceDaoImpl;
    }

    private void doBusinessVisitor(Business business, JSONObject businessVisitor) {

        autoSaveDelBusinessVisitor(business, businessVisitor);

    }
}

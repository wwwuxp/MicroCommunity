package com.hezong.pubcar.listener.repairform;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.PubcarRepairFormDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.entity.pubcar.BusinessPubcarRepairForm;
import com.java110.entity.pubcar.PubcarRepairFormFlow;
import com.java110.intf.pubcar.PubcarRepairFormFlowSMO;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lmf
 * @date 2022/7/28
 */
@Java110Listener("savePubcarRepaorFormListener")
@Transactional
public class SavePubcarRepaorFormListener extends AbstractPubCarRepairFormBusinessServiceDataFlowListener{

    @Autowired
    PubcarRepairFormDao pubcarRepairFormDao;

    @Autowired
    PubcarRepairFormFlowSMO pubcarRepairFormFlowSMO;

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 这里存Business数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");
        String simpleName = BusinessPubcarRepairForm.class.getSimpleName();
        if(data.containsKey(simpleName)){
            BusinessPubcarRepairForm pubcarRepairForm = data.getObject(simpleName, BusinessPubcarRepairForm.class);

            pubcarRepairForm.setbId(business.getbId());
            pubcarRepairForm.setOperate(StatusConstant.OPERATE_ADD);
            //新增默认为未审批
            pubcarRepairForm.setAuditStatus("1");
            //新增设定步长为0
            pubcarRepairForm.setFlowStep(0);
            pubcarRepairFormDao.saveBusinessPubcarRepairForm(pubcarRepairForm);
            //新增记录成功后，查询此流程的模板，然后组装流程数据
            pubcarRepairFormFlowSMO.insertFlow(new PubcarRepairFormFlow(pubcarRepairForm.getRepairId(), pubcarRepairForm.getUserId(), pubcarRepairForm.getUserName()));
            dataFlowContext.addParamOut("repairId",pubcarRepairForm.getRepairId());
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 申请修车信息
        List<BusinessPubcarRepairForm> businessPubcarRepairForm = pubcarRepairFormDao.getBusinessPubcarRepairFormInfo(info);
        if( businessPubcarRepairForm != null && businessPubcarRepairForm.size() >0) {
            pubcarRepairFormDao.savePubcarRepairFormInfo(info);
            if(businessPubcarRepairForm.size() == 1) {
                dataFlowContext.addParamOut("repairId", businessPubcarRepairForm.get(0).getRepairId());
            }
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_REPAIR_FROM;
    }

}

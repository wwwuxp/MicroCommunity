package com.hezong.pubcar.listener.repairform;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.PubcarRepairFormDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.entity.pubcar.BusinessPubcarRepairForm;
import com.java110.entity.pubcar.PubcarRepairForm;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lmf
 * @date 2022/7/28
 */
@Java110Listener("deletePubcarRepaorFormListener")
@Transactional
public class DeletePubcarRepaorFormListener extends AbstractPubCarRepairFormBusinessServiceDataFlowListener{

    @Autowired
    PubcarRepairFormDao pubcarRepairFormDao;

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
            PubcarRepairForm repairForm = pubcarRepairFormDao.queryById(pubcarRepairForm.getRepairId());
            if(repairForm == null){
                throw new ListenerExecuteException(HttpStatus.BAD_REQUEST.value(), "申请不存在");
            }
            if(repairForm.getAuditStatus().equals("1") || repairForm.getAuditStatus().equals("2")){
                throw new ListenerExecuteException(HttpStatus.BAD_REQUEST.value(), "仅能删除审核不通过和撤回的数据");
            }
            pubcarRepairForm.setbId(business.getbId());
            pubcarRepairForm.setOperate(StatusConstant.OPERATE_DEL);
            pubcarRepairFormDao.saveBusinessPubcarRepairForm(pubcarRepairForm);
            dataFlowContext.addParamOut("repairId",pubcarRepairForm.getRepairId());
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        // 申请修车信息
        List<BusinessPubcarRepairForm> businessPubcarRepairForm = pubcarRepairFormDao.getBusinessPubcarRepairFormInfo(info);
        if( businessPubcarRepairForm != null && businessPubcarRepairForm.size() >0) {
            for (int pubCarTripIndex = 0; pubCarTripIndex < businessPubcarRepairForm.size(); pubCarTripIndex++) {
                BusinessPubcarRepairForm entity = businessPubcarRepairForm.get(pubCarTripIndex);
                PubcarRepairForm pubcarRepairForm = new PubcarRepairForm();
                pubcarRepairForm.setRepairId(entity.getRepairId());
                pubcarRepairForm.setDeletedFlag(1);
                pubcarRepairForm.setbId(business.getbId());
                pubcarRepairFormDao.update(pubcarRepairForm);
                if(businessPubcarRepairForm.size() == 1) {
                    dataFlowContext.addParamOut("repairId", businessPubcarRepairForm.get(0).getRepairId());
                }
            }
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {

    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_REPAIR_FROM;
    }

}

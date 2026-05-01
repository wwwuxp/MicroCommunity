package com.hezong.pubcar.listener.repairform;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.PubcarRepairFormDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.entity.pubcar.BusinessPubcarRepairForm;
import com.java110.entity.pubcar.PubcarRepairForm;
import com.java110.entity.pubcar.PubcarRepairFormFlow;
import com.java110.intf.pubcar.PubcarRepairFormFlowSMO;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lmf
 * @date 2022/7/28
 */
@Java110Listener("eidtPubcarRepaorFormListener")
@Transactional
public class EidtPubcarRepaorFormListener extends AbstractPubCarRepairFormBusinessServiceDataFlowListener{

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
            String repairId = pubcarRepairForm.getRepairId();
            String auditStatus = pubcarRepairForm.getAuditStatus();
            PubcarRepairForm entity = pubcarRepairFormDao.queryById(repairId);
            if(entity != null){
                if(PubcarRepairFormFlowSMO.PAST.equals(entity.getAuditStatus()) || PubcarRepairFormFlowSMO.FAIL.equals(entity.getAuditStatus())){
                    throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "  已审批数据， 不可修改");
                }
            }else{
                throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "  修改失败， 数据不存在");
            }
            //在这里审批
            //把记录id，审批人id，是否通过审批，审批意见传过去
            int code = pubcarRepairFormFlowSMO.insertFlow(new PubcarRepairFormFlow(repairId, pubcarRepairForm.getUserId(), auditStatus, pubcarRepairForm.getApprovalComments()));
            switch (code){
                case PubcarRepairFormFlowSMO.END:
                    break;
                case PubcarRepairFormFlowSMO.NEXT:
                    pubcarRepairForm.setAuditStatus(PubcarRepairFormFlowSMO.NOT_APPROVED);
                    break;
                case PubcarRepairFormFlowSMO.ERR:
                    throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "出现错误，请检查是否为审批人");
            }
            pubcarRepairForm.setbId(business.getbId());
            pubcarRepairForm.setOperate(StatusConstant.OPERATE_MOD);
            // 设置步长
            pubcarRepairForm.setFlowStep(entity.getFlowStep() + 1);
            pubcarRepairFormDao.saveBusinessPubcarRepairForm(pubcarRepairForm);
            dataFlowContext.addParamOut("repairId", repairId);
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_MOD);

        // 申请修车信息
        List<BusinessPubcarRepairForm> businessPubcarRepairForm = pubcarRepairFormDao.getBusinessPubcarRepairFormInfo(info);
        if( businessPubcarRepairForm != null && businessPubcarRepairForm.size() >0) {
            for (int pubCarTripIndex = 0; pubCarTripIndex < businessPubcarRepairForm.size(); pubCarTripIndex++) {

                BusinessPubcarRepairForm entity = businessPubcarRepairForm.get(pubCarTripIndex);
                entity.setbId(business.getbId());
                PubcarRepairForm pubcarRepairForm = new PubcarRepairForm();
                BeanUtils.copyProperties(entity, pubcarRepairForm);

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
        return BusinessTypeConstant.BUSINESS_TYPE_EDIT_PUBCAR_REPAIR_FROM;
    }

}

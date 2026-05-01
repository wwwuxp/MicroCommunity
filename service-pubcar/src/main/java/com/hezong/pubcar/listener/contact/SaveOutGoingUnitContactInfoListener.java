package com.hezong.pubcar.listener.contact;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingUnitContactServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存外出单位联系人信息 侦听
 * @author hh
 * @datetime 2021/9/13
 */
@Java110Listener("saveOutGoingUnitContactInfoListener")
@Transactional
public class SaveOutGoingUnitContactInfoListener extends AbstractOutGoingUnitContactBusinessServiceDataFlowListener {

    @Autowired
    private IOutGoingUnitContactServiceDao outGoingUnitContactServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(OutGoingUnitContactPo.class.getSimpleName())){
            Object obj = data.get(OutGoingUnitContactPo.class.getSimpleName());
            JSONArray businessOutGoingUnitContacts;
            if(obj instanceof JSONObject){
                businessOutGoingUnitContacts = new JSONArray();
                businessOutGoingUnitContacts.add(obj);
            }else {
                businessOutGoingUnitContacts = (JSONArray)obj;
            }
            for (int outGoingUnitContactIndex = 0; outGoingUnitContactIndex < businessOutGoingUnitContacts.size();outGoingUnitContactIndex++) {
                JSONObject businessOutGoingUnitContact = businessOutGoingUnitContacts.getJSONObject(outGoingUnitContactIndex);
                doBusinessOutGoingUnitContact(business, businessOutGoingUnitContact);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("contactId", businessOutGoingUnitContact.getString("contactId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap(2);
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 外出单位联系人信息
        List<Map> businessOutGoingUnitContactInfo = outGoingUnitContactServiceDaoImpl.getBusinessOutGoingUnitContactInfo(info);
        if( businessOutGoingUnitContactInfo != null && businessOutGoingUnitContactInfo.size() >0) {
            outGoingUnitContactServiceDaoImpl.saveOutGoingUnitContactInfo(info);
            if(businessOutGoingUnitContactInfo.size() == 1) {
                dataFlowContext.addParamOut("contactId", businessOutGoingUnitContactInfo.get(0).get("contactId"));
            }
        }
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        // String bId = business.getbId();
        // Map<String, String> info = new HashMap<>();
        // info.put("bId", bId);
        // info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        // Map<String, String> paramIn = new HashMap<>();
        // paramIn.put("bId", bId);
        // paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        // // 异常报警信息
        // List<Map> pubCarAlarmInfo = outGoingServiceDaoImpl.getPubCarAlarmInstanceInfo(info);
        // if (CollectionUtils.isNotEmpty(pubCarAlarmInfo)) {
        //     outGoingServiceDaoImpl.updatePubCarAlarmInfo(paramIn);
        // }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OUT_GOING_UNIT_CONTACT;
    }

    @Override
    public IOutGoingUnitContactServiceDao getOutGoingUnitContactServiceDaoImpl() {
        return outGoingUnitContactServiceDaoImpl;
    }

    /**
     * 处理 businessOutGoingUnitContact 节点
     * @param business
     * @param businessOutGoingUnitContact
     */
    private void doBusinessOutGoingUnitContact(Business business, JSONObject businessOutGoingUnitContact){

        Assert.jsonObjectHaveKey(businessOutGoingUnitContact,"contactId","businessOutGoingUnitContact 节点下没有包含 contactId 节点");

        if(businessOutGoingUnitContact.getString("contactId").startsWith("-")){
            businessOutGoingUnitContact.put("contactId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_UNIT_CONTACT_ID));
        }

        businessOutGoingUnitContact.put("bId",business.getbId());
        businessOutGoingUnitContact.put("operate", StatusConstant.OPERATE_ADD);
        // 保存外出单位联系人信息
        outGoingUnitContactServiceDaoImpl.saveBusinessOutGoingUnitContactInfo(businessOutGoingUnitContact);

    }
}

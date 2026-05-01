package com.hezong.pubcar.listener.personnel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingPersonnelServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存外出人员信息 侦听
 * @author hh
 * @datetime 2021/9/13
 */
@Java110Listener("saveOutGoingPersonnelInfoListener")
@Transactional
public class SaveOutGoingPersonnelInfoListener extends AbstractOutGoingPersonnelBusinessServiceDataFlowListener {

    @Autowired
    private IOutGoingPersonnelServiceDao outGoingPersonnelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(OutGoingPersonnelPo.class.getSimpleName())){
            Object obj = data.get(OutGoingPersonnelPo.class.getSimpleName());
            JSONArray businessOutGoingPersonnels;
            if(obj instanceof JSONObject){
                businessOutGoingPersonnels = new JSONArray();
                businessOutGoingPersonnels.add(obj);
            }else {
                businessOutGoingPersonnels = (JSONArray)obj;
            }
            for (int outGoingPersonnelIndex = 0; outGoingPersonnelIndex < businessOutGoingPersonnels.size();outGoingPersonnelIndex++) {
                JSONObject businessOutGoingPersonnel = businessOutGoingPersonnels.getJSONObject(outGoingPersonnelIndex);
                doBusinessOutGoingPersonnel(business, businessOutGoingPersonnel);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("personnelId", businessOutGoingPersonnel.getString("personnelId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 外出人员信息
        List<Map> businessOutGoingPersonnelInfo = outGoingPersonnelServiceDaoImpl.getBusinessOutGoingPersonnelInfo(info);
        if( businessOutGoingPersonnelInfo != null && businessOutGoingPersonnelInfo.size() >0) {
            outGoingPersonnelServiceDaoImpl.saveOutGoingPersonnelInfo(info);
            if(businessOutGoingPersonnelInfo.size() == 1) {
                dataFlowContext.addParamOut("personnelId", businessOutGoingPersonnelInfo.get(0).get("personnelId"));
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
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OUT_GOING_PERSONNEL;
    }

    @Override
    public IOutGoingPersonnelServiceDao getOutGoingPersonnelServiceDaoImpl() {
        return outGoingPersonnelServiceDaoImpl;
    }

    /**
     * 处理 businessOutGoingPersonnel 节点
     * @param business
     * @param businessOutGoingPersonnel
     */
    private void doBusinessOutGoingPersonnel(Business business, JSONObject businessOutGoingPersonnel){

        Assert.jsonObjectHaveKey(businessOutGoingPersonnel,"personnelId","businessOutGoingPersonnel 节点下没有包含 personnelId 节点");

        if(businessOutGoingPersonnel.getString("personnelId").startsWith("-")){
            businessOutGoingPersonnel.put("personnelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_PERSONNEL_ID));
        }

        businessOutGoingPersonnel.put("bId",business.getbId());
        businessOutGoingPersonnel.put("operate", StatusConstant.OPERATE_ADD);
        // 保存外出人员信息
        outGoingPersonnelServiceDaoImpl.saveBusinessOutGoingPersonnelInfo(businessOutGoingPersonnel);

    }
}

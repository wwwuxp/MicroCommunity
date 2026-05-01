package com.hezong.pubcar.listener.alarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarAlarmServiceDao;
import com.hezong.pubcar.dao.IPubCarDriverServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存异常报警信息 侦听
 * @author hh
 * @datetime 2021/8/7 10:48
 */
@Java110Listener("savePubCarAlarmInfoListener")
@Transactional
public class SavePubCarAlarmInfoListener extends AbstractPubCarAlarmBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarAlarmServiceDao pubCarAlarmServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarAlarmPo.class.getSimpleName())){
            Object obj = data.get(PubCarAlarmPo.class.getSimpleName());
            JSONArray businessPubCarAlarms = null;
            if(obj instanceof JSONObject){
                businessPubCarAlarms = new JSONArray();
                businessPubCarAlarms.add(obj);
            }else {
                businessPubCarAlarms = (JSONArray)obj;
            }
            for (int pubCarAlarmIndex = 0; pubCarAlarmIndex < businessPubCarAlarms.size();pubCarAlarmIndex++) {
                JSONObject businessPubCarAlarm = businessPubCarAlarms.getJSONObject(pubCarAlarmIndex);
                doBusinessAlarm(business, businessPubCarAlarm);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("alarmId", businessPubCarAlarm.getString("alarmId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 异常报警信息
        List<Map> businessPubCarAlarmInfo = pubCarAlarmServiceDaoImpl.getBusinessPubCarAlarmInfo(info);
        if( businessPubCarAlarmInfo != null && businessPubCarAlarmInfo.size() >0) {
            pubCarAlarmServiceDaoImpl.savePubCarAlarmInfo(info);
            if(businessPubCarAlarmInfo.size() == 1) {
                dataFlowContext.addParamOut("alarmId", businessPubCarAlarmInfo.get(0).get("alarmId"));
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
        String bId = business.getbId();
        Map<String, String> info = new HashMap<>();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map<String, String> paramIn = new HashMap<>();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        // 异常报警信息
        List<Map> pubCarAlarmInfo = pubCarAlarmServiceDaoImpl.getPubCarAlarmInstanceInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarAlarmInfo)) {
            pubCarAlarmServiceDaoImpl.updatePubCarAlarmInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_ALARM;
    }

    @Override
    public IPubCarAlarmServiceDao getPubCarAlarmServiceDaoImpl() {
        return pubCarAlarmServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarAlarm
     */
    private void doBusinessAlarm(Business business, JSONObject businessPubCarAlarm){

        Assert.jsonObjectHaveKey(businessPubCarAlarm,"alarmId","businessPubCarAlarm 节点下没有包含 alarmId 节点");

        if(businessPubCarAlarm.getString("alarmId").startsWith("-")){
            businessPubCarAlarm.put("alarmId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ALARM_ID));
        }


        businessPubCarAlarm.put("bId",business.getbId());
        businessPubCarAlarm.put("operate", StatusConstant.OPERATE_ADD);
        // 保存异常报警信息
        pubCarAlarmServiceDaoImpl.saveBusinessPubCarAlarmInfo(businessPubCarAlarm);

    }
}

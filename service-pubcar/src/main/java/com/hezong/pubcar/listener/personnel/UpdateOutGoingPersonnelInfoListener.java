package com.hezong.pubcar.listener.personnel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingPersonnelServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改异常报警信息 侦听
 * @author hh
 * @datetime 2021/9/13
 */
@Java110Listener("updateOutGoingPersonnelInfoListener")
@Transactional
public class UpdateOutGoingPersonnelInfoListener extends AbstractOutGoingPersonnelBusinessServiceDataFlowListener {

    @Autowired
    private IOutGoingPersonnelServiceDao outGoingPersonnelServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
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
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);


        // List<Map> businessPubCarAlarmInfos = outGoingServiceDaoImpl.getBusinessPubCarAlarmInfo(info);
        // if (businessPubCarAlarmInfos != null && businessPubCarAlarmInfos.size() > 0) {
        //     for (int pubCarAlarmIndex = 0; pubCarAlarmIndex < businessPubCarAlarmInfos.size(); pubCarAlarmIndex++) {
        //
        //         Map businessPubCarAlarmInfo = businessPubCarAlarmInfos.get(pubCarAlarmIndex);
        //         flushBusinessOutGoingInfo(businessPubCarAlarmInfo, StatusConstant.STATUS_CD_VALID);
        //
        //         outGoingServiceDaoImpl.updatePubCarAlarmInfo(businessPubCarAlarmInfo);
        //         if (businessPubCarAlarmInfo.size() == 1) {
        //             dataFlowContext.addParamOut("alarmId", businessPubCarAlarmInfo.get("alarmId"));
        //         }
        //     }
        // }
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
        // Map<String, String> delInfo = new HashMap<>();
        // delInfo.put("bId", business.getbId());
        // delInfo.put("operate", StatusConstant.OPERATE_DEL);
        // // 异常报警信息
        // List<Map> pubCarAlarmInfo = outGoingServiceDaoImpl.getPubCarAlarmInstanceInfo(info);
        // if (CollectionUtils.isNotEmpty(pubCarAlarmInfo)) {
        //
        //     // 异常报警信息
        //     List<Map> businessPubCarAlarmInfos = outGoingServiceDaoImpl.getBusinessPubCarAlarmInfo(delInfo);
        //     // 除非程序出错了，这里不会为空
        //     if (CollectionUtils.isEmpty(businessPubCarAlarmInfos)) {
        //         throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarAlarm），程序内部异常,请检查！ " + delInfo);
        //     }
        //     for (int pubCarAlarmIndex = 0; pubCarAlarmIndex < businessPubCarAlarmInfos.size(); pubCarAlarmIndex++) {
        //         Map businessPubCarAlarmInfo = businessPubCarAlarmInfos.get(pubCarAlarmIndex);
        //         flushBusinessOutGoingInfo(businessPubCarAlarmInfo, StatusConstant.STATUS_CD_VALID);
        //         outGoingServiceDaoImpl.updatePubCarAlarmInfo(businessPubCarAlarmInfo);
        //     }
        // }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_ALARM;
    }

    @Override
    public IOutGoingPersonnelServiceDao getOutGoingPersonnelServiceDaoImpl() {
        return outGoingPersonnelServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarAlarm
     */
    private void doBusinessAlarm(Business business, JSONObject businessPubCarAlarm){

        // Assert.jsonObjectHaveKey(businessPubCarAlarm,"alarmId","businessPubCarAlarm 节点下没有包含 alarmId 节点");
        //
        // if(businessPubCarAlarm.getString("alarmId").startsWith("-")){
        //     businessPubCarAlarm.put("alarmId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ALARM_ID));
        // }
        //
        // businessPubCarAlarm.put("bId",business.getbId());
        // businessPubCarAlarm.put("operate", StatusConstant.OPERATE_MOD);
        //
        // // 保存异常报警业务表信息
        // outGoingServiceDaoImpl.saveBusinessPubCarAlarmInfo(businessPubCarAlarm);

    }
}

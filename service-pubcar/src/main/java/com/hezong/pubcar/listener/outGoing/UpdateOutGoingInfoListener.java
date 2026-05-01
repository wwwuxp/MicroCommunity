package com.hezong.pubcar.listener.outGoing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改外出信息 侦听
 * @author hh
 * @datetime 2021/9/10
 */
@Java110Listener("updateOutGoingInfoListener")
@Transactional
public class UpdateOutGoingInfoListener extends AbstractOutGoingBusinessServiceDataFlowListener {

    @Autowired
    private IOutGoingServiceDao outGoingServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(OutGoingPo.class.getSimpleName())){
            Object obj = data.get(OutGoingPo.class.getSimpleName());
            JSONArray businessOutGoings;
            if(obj instanceof JSONObject){
                businessOutGoings = new JSONArray();
                businessOutGoings.add(obj);
            }else {
                businessOutGoings = (JSONArray)obj;
            }
            for (int outGoingIndex = 0; outGoingIndex < businessOutGoings.size();outGoingIndex++) {
                JSONObject businessOutGoing = businessOutGoings.getJSONObject(outGoingIndex);
                doBusinessOutGoing(business, businessOutGoing);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("outId", businessOutGoing.getString("outId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap(2);
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);

        List<Map> businessOutGoingInfos = outGoingServiceDaoImpl.getBusinessOutGoingInfo(info);
        if (businessOutGoingInfos != null && businessOutGoingInfos.size() > 0) {
            for (int outGoingIndex = 0; outGoingIndex < businessOutGoingInfos.size(); outGoingIndex++) {

                Map businessOutGoingInfo = businessOutGoingInfos.get(outGoingIndex);
                flushBusinessOutGoingInfo(businessOutGoingInfo, StatusConstant.STATUS_CD_VALID);

                outGoingServiceDaoImpl.updateOutGoingInfo(businessOutGoingInfo);
                if (businessOutGoingInfo.size() == 1) {
                    dataFlowContext.addParamOut("outId", businessOutGoingInfo.get("outId"));
                }
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
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OUT_GOING;
    }

    @Override
    public IOutGoingServiceDao getOutGoingServiceDaoImpl() {
        return outGoingServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessOutGoing
     */
    private void doBusinessOutGoing(Business business, JSONObject businessOutGoing){

        Assert.jsonObjectHaveKey(businessOutGoing,"outId","businessOutGoing 节点下没有包含 outId 节点");

        if(businessOutGoing.getString("outId").startsWith("-")){
            businessOutGoing.put("outId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_OUT_GOING_ID));
        }

        businessOutGoing.put("bId",business.getbId());
        businessOutGoing.put("operate", StatusConstant.OPERATE_MOD);

        // 保存异常报警业务表信息
        outGoingServiceDaoImpl.saveBusinessOutGoingInfo(businessOutGoing);

    }
}

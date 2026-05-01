package com.hezong.pubcar.listener.trip;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改行程信息 侦听
 * @author hh
 * @datetime 2021/7/23 10:48
 */
@Java110Listener("updatePubCarTripInfoListener")
@Transactional
public class UpdatePubCarTripInfoListener extends AbstractPubCarTripBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripServiceDao pubCarTripServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        if (data.containsKey(PubCarTripPo.class.getSimpleName())) {
            Object obj = data.get(PubCarTripPo.class.getSimpleName());
            JSONArray businessPubCarTrips = null;
            if (obj instanceof JSONObject) {
                businessPubCarTrips = new JSONArray();
                businessPubCarTrips.add(obj);
            } else {
                businessPubCarTrips = (JSONArray) obj;
            }
            for (int pubCarTripIndex = 0; pubCarTripIndex < businessPubCarTrips.size(); pubCarTripIndex++) {
                JSONObject businessPubCarTrip = businessPubCarTrips.getJSONObject(pubCarTripIndex);
                doBusinessPubCarTrip(business, businessPubCarTrip);
                if (obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("tripId", businessPubCarTrip.getString("tripId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);


        List<Map> businessPubCarTripInfos = pubCarTripServiceDaoImpl.getBusinessPubCarTripInfo(info);
        if (businessPubCarTripInfos != null && businessPubCarTripInfos.size() > 0) {
            for (int pubCarTripIndex = 0; pubCarTripIndex < businessPubCarTripInfos.size(); pubCarTripIndex++) {

                Map businessPubCarTripInfo = businessPubCarTripInfos.get(pubCarTripIndex);
                flushBusinessPubCarTripInfo(businessPubCarTripInfo, StatusConstant.STATUS_CD_VALID);

                pubCarTripServiceDaoImpl.updatePubCarTripInfo(businessPubCarTripInfo);
                if (businessPubCarTripInfo.size() == 1) {
                    dataFlowContext.addParamOut("tripId", businessPubCarTripInfo.get("tripId"));
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
        String bId = business.getbId();
        Map<String, String> info = new HashMap<>();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map<String, String> delInfo = new HashMap<>();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        // 行程信息
        List<Map> pubCarTripInfo = pubCarTripServiceDaoImpl.getPubCarTripInstanceInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarTripInfo)) {

            // 行程信息
            List<Map> businessPubCarTripInfos = pubCarTripServiceDaoImpl.getBusinessPubCarTripInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarTripInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarTrip），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarTripIndex = 0; pubCarTripIndex < businessPubCarTripInfos.size(); pubCarTripIndex++) {
                Map businessPubCarTripInfo = businessPubCarTripInfos.get(pubCarTripIndex);
                flushBusinessPubCarTripInfo(businessPubCarTripInfo, StatusConstant.STATUS_CD_VALID);
                pubCarTripServiceDaoImpl.updatePubCarTripInfo(businessPubCarTripInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP;
    }

    @Override
    public IPubCarTripServiceDao getPubCarTripServiceDaoImpl() {
        return pubCarTripServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     *
     * @param business
     * @param businessPubCarTrip
     */
    private void doBusinessPubCarTrip(Business business, JSONObject businessPubCarTrip) {

        Assert.jsonObjectHaveKey(businessPubCarTrip, "tripId", "businessPubCarTrip 节点下没有包含 tripId 节点");

        if (businessPubCarTrip.getString("tripId").startsWith("-")) {
            businessPubCarTrip.put("tripId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_ID));
        }

        businessPubCarTrip.put("approvalStatus", businessPubCarTrip.getInteger("state"));
        businessPubCarTrip.put("bId", business.getbId());
        businessPubCarTrip.put("operate", StatusConstant.OPERATE_MOD);

        // 保存行程业务表信息
        pubCarTripServiceDaoImpl.saveBusinessPubCarTripInfo(businessPubCarTrip);

    }
}

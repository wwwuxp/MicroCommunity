package com.hezong.pubcar.listener.mileage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripMileageServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
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
 * 修改行程里程信息 侦听
 * @author hh
 * @datetime 2021/7/23 10:48
 */
@Java110Listener("updatePubCarTripMileageInfoListener")
@Transactional
public class UpdatePubCarTripMileageInfoListener extends AbstractPubCarTripMileageBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripMileageServiceDao pubCarTripMileageServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        if (data.containsKey(PubCarTripMileagePo.class.getSimpleName())) {
            Object obj = data.get(PubCarTripMileagePo.class.getSimpleName());
            JSONArray businessTripMileages;
            if (obj instanceof JSONObject) {
                businessTripMileages = new JSONArray();
                businessTripMileages.add(obj);
            } else {
                businessTripMileages = (JSONArray) obj;
            }
            for (int tripMileageIndex = 0; tripMileageIndex < businessTripMileages.size(); tripMileageIndex++) {
                JSONObject businessTripMileage = businessTripMileages.getJSONObject(tripMileageIndex);
                doBusinessTripMileage(business, businessTripMileage);
                if (obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("mileageId", businessTripMileage.getString("mileageId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);

        List<Map> businessPubCarTripMileageInfos = pubCarTripMileageServiceDaoImpl.getBusinessPubCarTripMileageInfo(info);
        if (businessPubCarTripMileageInfos != null && businessPubCarTripMileageInfos.size() > 0) {
            for (Map businessPubCarTripMileageInfo : businessPubCarTripMileageInfos) {

                flushBusinessPubCarTripMileageInfo(businessPubCarTripMileageInfo, StatusConstant.STATUS_CD_VALID);

                pubCarTripMileageServiceDaoImpl.updatePubCarTripMileageInfo(businessPubCarTripMileageInfo);
                if (businessPubCarTripMileageInfo.size() == 1) {
                    dataFlowContext.addParamOut("mileageId", businessPubCarTripMileageInfo.get("mileageId"));
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
        // 行程里程信息
        List<Map> pubCarTripMileageInfo = pubCarTripMileageServiceDaoImpl.getPubCarTripMileageInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarTripMileageInfo)) {

            // 行程里程信息
            List<Map> businessPubCarTripMileageInfos = pubCarTripMileageServiceDaoImpl.getBusinessPubCarTripMileageInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarTripMileageInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarTripMileage），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarTripMileageIndex = 0; pubCarTripMileageIndex < businessPubCarTripMileageInfos.size(); pubCarTripMileageIndex++) {
                Map businessPubCarTripMileageInfo = businessPubCarTripMileageInfos.get(pubCarTripMileageIndex);
                flushBusinessPubCarTripMileageInfo(businessPubCarTripMileageInfo, StatusConstant.STATUS_CD_VALID);
                pubCarTripMileageServiceDaoImpl.updatePubCarTripMileageInfo(businessPubCarTripMileageInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP_MILEAGE;
    }

    @Override
    public IPubCarTripMileageServiceDao getPubCarTripMileageServiceDaoImpl() {
        return pubCarTripMileageServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     *
     * @param business
     * @param businessPubCarTripMileage
     */
    private void doBusinessTripMileage(Business business, JSONObject businessPubCarTripMileage) {

        Assert.jsonObjectHaveKey(businessPubCarTripMileage, "tripId", "businessPubCarTripMileage 节点下没有包含 tripId 节点");
        Assert.jsonObjectHaveKey(businessPubCarTripMileage, "driverId", "businessPubCarTripMileage 节点下没有包含 driverId 节点");
        Assert.jsonObjectHaveKey(businessPubCarTripMileage, "communityId", "businessPubCarTripMileage 节点下没有包含 communityId 节点");

        businessPubCarTripMileage.put("bId", business.getbId());
        businessPubCarTripMileage.put("operate", StatusConstant.OPERATE_MOD);

        // 保存行程里程业务表信息
        pubCarTripMileageServiceDaoImpl.saveBusinessPubCarTripMileageInfo(businessPubCarTripMileage);

    }
}

package com.hezong.pubcar.listener.driver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarDriverServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarDriver.PubCarDriverPo;
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
 * 修改司机信息 侦听
 *
 * @author hh
 * @datetime 2021/7/23 10:48
 */
@Java110Listener("updatePubCarDriverInfoListener")
@Transactional
public class UpdatePubCarDriverInfoListener extends AbstractPubCarDriverBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarDriverServiceDao pubCarDriverServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        if (data.containsKey(PubCarDriverPo.class.getSimpleName())) {
            Object obj = data.get(PubCarDriverPo.class.getSimpleName());
            JSONArray businessPubCarDrivers = null;
            if (obj instanceof JSONObject) {
                businessPubCarDrivers = new JSONArray();
                businessPubCarDrivers.add(obj);
            } else {
                businessPubCarDrivers = (JSONArray) obj;
            }
            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarDrivers.size(); pubCarDriverIndex++) {
                JSONObject businessPubCarDriver = businessPubCarDrivers.getJSONObject(pubCarDriverIndex);
                doBusinessPubCarDriver(business, businessPubCarDriver);
                if (obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("driverId", businessPubCarDriver.getString("driverId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);


        List<Map> businessPubCarDriverInfos = pubCarDriverServiceDaoImpl.getBusinessPubCarDriverInfo(info);
        if (businessPubCarDriverInfos != null && businessPubCarDriverInfos.size() > 0) {
            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarDriverInfos.size(); pubCarDriverIndex++) {

                Map businessPubCarDriverInfo = businessPubCarDriverInfos.get(pubCarDriverIndex);
                flushBusinessPubCarDriverInfo(businessPubCarDriverInfo, StatusConstant.STATUS_CD_VALID);

                pubCarDriverServiceDaoImpl.updatePubCarDriverInfo(businessPubCarDriverInfo);
                if (businessPubCarDriverInfo.size() == 1) {
                    dataFlowContext.addParamOut("driverId", businessPubCarDriverInfo.get("driverId"));
                }
            }
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map<String, String> info = new HashMap<>();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map<String, String> delInfo = new HashMap<>();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        // 司机信息
        List<Map> pubCarDriverInfo = pubCarDriverServiceDaoImpl.getPubCarDriverInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarDriverInfo)) {

            // 司机信息
            List<Map> businessPubCarDriverInfos = pubCarDriverServiceDaoImpl.getBusinessPubCarDriverInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarDriverInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarDriver），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarDriverInfos.size(); pubCarDriverIndex++) {
                Map businessPubCarDriverInfo = businessPubCarDriverInfos.get(pubCarDriverIndex);
                flushBusinessPubCarDriverInfo(businessPubCarDriverInfo, StatusConstant.STATUS_CD_VALID);
                pubCarDriverServiceDaoImpl.updatePubCarDriverInfo(businessPubCarDriverInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_DRIVER;
    }

    @Override
    public IPubCarDriverServiceDao getPubCarDriverServiceDaoImpl() {
        return pubCarDriverServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     *
     * @param business
     * @param businessPubCarDriver
     */
    private void doBusinessPubCarDriver(Business business, JSONObject businessPubCarDriver) {

        Assert.jsonObjectHaveKey(businessPubCarDriver, "driverId", "businessPubCarDriver 节点下没有包含 driverId 节点");

        if (businessPubCarDriver.getString("driverId").startsWith("-")) {
            businessPubCarDriver.put("driverId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_DRIVER_ID));
        }

        businessPubCarDriver.put("bId", business.getbId());
        businessPubCarDriver.put("operate", StatusConstant.OPERATE_MOD);

        // 保存司机业务信息
        pubCarDriverServiceDaoImpl.saveBusinessPubCarDriverInfo(businessPubCarDriver);

    }
}

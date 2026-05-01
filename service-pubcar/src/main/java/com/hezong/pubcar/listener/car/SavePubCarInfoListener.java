package com.hezong.pubcar.listener.car;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
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
 * 保存车辆信息 侦听
 *
 * @author hh
 * @datetime 2021/7/15 15:49
 */
@Java110Listener("savePubCarInfoListener")
@Transactional
public class SavePubCarInfoListener extends AbstractPubCarBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarServiceDao pubCarServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        if (data.containsKey("pubCar")) {
            Object bObj = data.get("pubCar");
            JSONArray businessPubCars = null;
            if (bObj instanceof JSONObject) {
                businessPubCars = new JSONArray();
                businessPubCars.add(bObj);
            } else {
                businessPubCars = (JSONArray) bObj;
            }
            for (int bPubCarIndex = 0; bPubCarIndex < businessPubCars.size(); bPubCarIndex++) {
                JSONObject businessPubCar = businessPubCars.getJSONObject(bPubCarIndex);
                doBusinessPubCar(business, businessPubCar);
                if (bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("carId", businessPubCar.getString("carId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map<String, Object> info = new HashMap<>();
        // 此bId是pubCar对应的b_id
        info.put("carBId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_ADD);

        //公车信息
        List<Map> businessPubCarInfo = pubCarServiceDaoImpl.getBusinessPubCarInfo(info);

        if (businessPubCarInfo != null && businessPubCarInfo.size() > 0) {
            Map businessPubCarInfoMap = businessPubCarInfo.get(0);
            reFreshShareColumn(info, businessPubCarInfoMap);

            // 获取business_pubcar_attr表的b_id,business_pubcar_insurance表的b_id
            String attrBId = (String) businessPubCarInfoMap.get("attrBId");
            String insuranceBId = (String) businessPubCarInfoMap.get("insuranceBId");
            info.put("attrBId", attrBId);
            info.put("insuranceBId", insuranceBId);

            pubCarServiceDaoImpl.savePubCarInfo(info);
            if (businessPubCarInfo.size() == 1) {
                dataFlowContext.addParamOut("carId", businessPubCarInfo.get(0).get("carId"));
            }
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map<String, String> info = new HashMap<>();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map<String, String> paramIn = new HashMap<>();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        // 车辆信息
        List<Map> pubCarInfo = pubCarServiceDaoImpl.getPubCarInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarInfo)) {
            pubCarServiceDaoImpl.updatePubCarInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR;
    }

    @Override
    public IPubCarServiceDao getPubCarServiceDaoImpl() {
        return pubCarServiceDaoImpl;
    }

    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {
        if (info.containsKey("communityId")) {
            return;
        }
        if (!businessInfo.containsKey("community_id")) {
            return;
        }
        info.put("communityId", businessInfo.get("community_id"));
    }

    /**
     * 处理 businessPubCar 节点
     *
     * @param business       总的数据节点
     * @param businessPubCar 费用配置节点
     */
    private void doBusinessPubCar(Business business, JSONObject businessPubCar) {

        Assert.jsonObjectHaveKey(businessPubCar, "carId", "businessPubCar 节点下没有包含 carId 节点");
        Assert.jsonObjectHaveKey(businessPubCar, "attrId", "businessPubCar 节点下没有包含 attrId 节点");
        Assert.jsonObjectHaveKey(businessPubCar, "insuranceId", "businessPubCar 节点下没有包含 insuranceId 节点");

        if (businessPubCar.getString("carId").startsWith("-")) {
            businessPubCar.put("carId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ID));
        }

        if (businessPubCar.getString("attrId").startsWith("-")) {
            businessPubCar.put("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ATTR));
        }

        if (businessPubCar.getString("insuranceId").startsWith("-")) {
            businessPubCar.put("insuranceId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_INSURANCE));
        }

        businessPubCar.put("carBId", business.getbId());

        JSONObject attr = (JSONObject) businessPubCar.get("attr");
        attr.put("attrBId", GenerateCodeFactory.getBId());
        attr.put("operate", StatusConstant.OPERATE_ADD);
        JSONObject insurance = (JSONObject) businessPubCar.get("insurance");
        insurance.put("insuranceBId", GenerateCodeFactory.getBId());
        insurance.put("operate", StatusConstant.OPERATE_ADD);

        businessPubCar.put("operate", StatusConstant.OPERATE_ADD);
        //保存公车信息
        pubCarServiceDaoImpl.saveBusinessPubCarInfo(businessPubCar);

    }
}

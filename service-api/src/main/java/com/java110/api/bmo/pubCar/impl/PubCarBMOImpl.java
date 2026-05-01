package com.java110.api.bmo.pubCar.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.ApiBaseBMO;
import com.java110.api.bmo.pubCar.IPubCarBMO;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @datetime 2021/7/15 11:31
 */
@Service("pubCarBMOImpl")
public class PubCarBMOImpl extends ApiBaseBMO implements IPubCarBMO {

    @Autowired
    private IPubCarInnerServiceSMO pubCarInnerServiceSMOImpl;

    /**
     * 添加车辆信息
     *
     * @param paramInJson 请求参数
     * @param context     context
     */
    @Override
    public void addCar(JSONObject paramInJson, DataFlowContext context) {
        paramInJson.fluentPut("carId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ID))
                .fluentPut("attrId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ATTR))
                .fluentPut("insuranceId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_INSURANCE));
        paramInJson.getJSONObject("attr").put("attrId", paramInJson.getString("attrId"));
        JSONObject insuranceJsonObject = paramInJson.getJSONObject("insurance");
        String maintenanceFee = insuranceJsonObject.getString("maintenanceFee");
        if (StringUtils.isBlank(maintenanceFee)) {
            maintenanceFee = "0.00";
        }
        insuranceJsonObject.put("insuranceId", paramInJson.getString("insuranceId"));
        insuranceJsonObject.put("maintenanceFee", new BigDecimal(maintenanceFee));
        handlePubCarDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR);
    }

    /**
     * 保存车辆信息
     *
     * @param paramInJson
     * @param context
     */
    @Override
    public void updatePubCar(JSONObject paramInJson, DataFlowContext context) {
        Map<String, Object> attrIdAndInsuranceIdMap = getAttrIdAndInsuranceIdMap(paramInJson);

        paramInJson.fluentPut("carId", paramInJson.getString("carId"))
                .fluentPut("attrId", attrIdAndInsuranceIdMap.get("attrId"))
                .fluentPut("insuranceId", attrIdAndInsuranceIdMap.get("insuranceId"));
        paramInJson.getJSONObject("attr").put("attrId", paramInJson.getString("attrId"));

        JSONObject insuranceJsonObject = paramInJson.getJSONObject("insurance");
        insuranceJsonObject.put("insuranceId", paramInJson.getString("insuranceId"));
        String maintenanceFee = insuranceJsonObject.getString("maintenanceFee");
        if (StringUtils.isBlank(maintenanceFee)) {
            maintenanceFee = "0.00";
        }
        insuranceJsonObject.put("maintenanceFee", new BigDecimal(maintenanceFee));
        handlePubCarDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR);
    }

    @Override
    public void deletePubCar(JSONObject paramInJson, DataFlowContext context) {
        Map<String, Object> attrIdAndInsuranceIdMap = getAttrIdAndInsuranceIdMap(paramInJson);

        JSONObject businessPubCar = new JSONObject();
        businessPubCar.fluentPut("carId", paramInJson.getString("carId"))
                .fluentPut("attrId", attrIdAndInsuranceIdMap.get("attrId"))
                .fluentPut("insuranceId", attrIdAndInsuranceIdMap.get("insuranceId"));
        handlePubCarDatas(context, businessPubCar, BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR);
    }

    @Override
    public void confirmPubCar(JSONObject paramInJson, DataFlowContext context) {
        Map<String, Object> attrIdAndInsuranceIdMap = getAttrIdAndInsuranceIdMap(paramInJson);

        paramInJson.put("attrId", attrIdAndInsuranceIdMap.get("attrId"));
        paramInJson.put("insuranceId", attrIdAndInsuranceIdMap.get("insuranceId"));
        paramInJson.put("confirmFlag", -1);
        if (paramInJson.containsKey("insuranceExpirationDate")){
            Date insuranceExpirationDate = paramInJson.getDate("insuranceExpirationDate");
            Date addOneYearDate = DateUtils.addYears(insuranceExpirationDate, 1);
            paramInJson.put("insuranceExpirationDate", DateFormatUtils.format(addOneYearDate, "yyyy-MM-dd"));
        }
        handlePubCarDatas(context, paramInJson, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR);
    }

    /**
     * 新增数据方法
     *
     * @param context 上下文对象
     * @param param   po对象
     */
    private void handlePubCarDatas(DataFlowContext context, JSONObject param, String businessType) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, businessType);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONArray businessArr = new JSONArray();
        businessArr.add(param);
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("pubCar", businessArr);
        context.addServiceBusiness(business);
    }

    /**
     * 获取公车的attrId和insuranceId
     *
     * @param paramInJson
     * @return
     */
    private Map<String, Object> getAttrIdAndInsuranceIdMap(JSONObject paramInJson) {
        // 查询公车的attrId,insuranceId
        List<Map<String, Object>> attrIdAndInsuranceId = pubCarInnerServiceSMOImpl.queryInfoByCarId(paramInJson.getString("carId"));
        if (CollectionUtils.isEmpty(attrIdAndInsuranceId)) {
            throw new BusinessException(ResponseConstant.RESULT_CODE_INNER_ERROR, "未找到相关公车信息");
        }

        Map<String, Object> attrIdAndInsuranceIdMap = attrIdAndInsuranceId.get(0);
        return attrIdAndInsuranceIdMap;
    }

}

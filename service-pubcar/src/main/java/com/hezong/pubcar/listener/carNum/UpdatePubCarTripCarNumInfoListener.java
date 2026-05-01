package com.hezong.pubcar.listener.carNum;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripCarNumServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import com.java110.po.pubCarTripCarNum.PubCarTripCarNumPo;
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
 * 修改车辆数目信息 侦听
 * @author hh
 * @datetime 2021/7/23 10:48
 */
@Java110Listener("updatePubCarTripCarNumInfoListener")
@Transactional
public class UpdatePubCarTripCarNumInfoListener extends AbstractPubCarTripCarNumBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarTripCarNumServiceDao pubCarTripCarNumServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarTripCarNumPo.class.getSimpleName())){
            Object obj = data.get(PubCarTripCarNumPo.class.getSimpleName());
            JSONArray businessTripCarNums = null;
            if(obj instanceof JSONObject){
                businessTripCarNums = new JSONArray();
                businessTripCarNums.add(obj);
            }else {
                businessTripCarNums = (JSONArray)obj;
            }
            for (int tripCarNumIndex = 0; tripCarNumIndex < businessTripCarNums.size();tripCarNumIndex++) {
                JSONObject businessTripCarNum = businessTripCarNums.getJSONObject(tripCarNumIndex);
                doBusinessTripCarNum(business, businessTripCarNum);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("numId", businessTripCarNum.getString("numId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_MOD);


        List<Map> businessTripCarNumInfos = pubCarTripCarNumServiceDaoImpl.getBusinessTripCarNumInfo(info);
        if (businessTripCarNumInfos != null && businessTripCarNumInfos.size() > 0) {
            for (int tripCarNumIndex = 0; tripCarNumIndex < businessTripCarNumInfos.size(); tripCarNumIndex++) {

                Map businessTripCarNumInfo = businessTripCarNumInfos.get(tripCarNumIndex);
                flushBusinessTripCarNumInfo(businessTripCarNumInfo, StatusConstant.STATUS_CD_VALID);

                pubCarTripCarNumServiceDaoImpl.updatePubCarTripCarNumInfo(businessTripCarNumInfo);
                if (businessTripCarNumInfo.size() == 1) {
                    dataFlowContext.addParamOut("numId", businessTripCarNumInfo.get("numId"));
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
        // 车辆数目信息
        List<Map> pubCarTripCarNumInfo = pubCarTripCarNumServiceDaoImpl.getPubCarTripCarNumInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarTripCarNumInfo)) {

            // 车辆数目信息
            List<Map> businessPubCarTripCarNumInfos = pubCarTripCarNumServiceDaoImpl.getBusinessTripCarNumInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarTripCarNumInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarTripCarNum），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarTripCarNumIndex = 0; pubCarTripCarNumIndex < businessPubCarTripCarNumInfos.size(); pubCarTripCarNumIndex++) {
                Map businessPubCarTripCarNumInfo = businessPubCarTripCarNumInfos.get(pubCarTripCarNumIndex);
                flushBusinessTripCarNumInfo(businessPubCarTripCarNumInfo, StatusConstant.STATUS_CD_VALID);
                pubCarTripCarNumServiceDaoImpl.updatePubCarTripCarNumInfo(businessPubCarTripCarNumInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_PUBCAR_TRIP_CAR_NUM;
    }

    @Override
    public IPubCarTripCarNumServiceDao getPubCarTripCarNumServiceDaoImpl() {
        return pubCarTripCarNumServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessTripCarNum
     */
    private void doBusinessTripCarNum(Business business, JSONObject businessTripCarNum){

//        Assert.jsonObjectHaveKey(businessTripCarNum,"numId","businessTripCarNum 节点下没有包含 numId 节点");
//
//        if(businessTripCarNum.getString("numId").startsWith("-")){
//            businessTripCarNum.put("numId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_CAR_NUM));
//        }

        businessTripCarNum.put("bId",business.getbId());
        businessTripCarNum.put("operate", StatusConstant.OPERATE_MOD);

        pubCarTripCarNumServiceDaoImpl.saveBusinessPubCarTripCarNumInfo(businessTripCarNum);

    }
}

package com.hezong.pubcar.listener.car;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.floor.FloorPo;
import com.java110.po.pubCar.PubCarPo;
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
 * 删除车辆信息 侦听
 * @author hh
 * @datetime 2021/7/15 15:49
 */
@Java110Listener("deletePubCarInfoListener")
@Transactional
public class DeletePubCarInfoListener extends AbstractPubCarBusinessServiceDataFlowListener{

    @Autowired
    private IPubCarServiceDao pubCarServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Assert.notEmpty(data, "没有datas 节点，或没有子节点需要处理");

        //处理 businessPubCar 节点
        if (data.containsKey("pubCar")) {
            Object obj = data.get("pubCar");
            JSONArray businessPubCars = null;
            if (obj instanceof JSONObject) {
                businessPubCars = new JSONArray();
                businessPubCars.add(obj);
            } else {
                businessPubCars = (JSONArray) obj;
            }
            for (int pubCarIndex = 0; pubCarIndex < businessPubCars.size(); pubCarIndex++) {
                JSONObject businessPubCar = businessPubCars.getJSONObject(pubCarIndex);
                doBusinessPubCar(business, businessPubCar);
                if (obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("carId", businessPubCar.getString("carId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        Map info = new HashMap();
        info.put("carBId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);

        List<Map> businessPubCarInfos = pubCarServiceDaoImpl.getBusinessPubCarInfo(info);
        if (businessPubCarInfos != null && businessPubCarInfos.size() > 0) {
            for (int pubCarIndex = 0; pubCarIndex < businessPubCarInfos.size(); pubCarIndex++) {
                Map businessPubCarInfo = businessPubCarInfos.get(pubCarIndex);
                flushBusinessPubCarInfo(businessPubCarInfo, StatusConstant.STATUS_CD_INVALID);
                pubCarServiceDaoImpl.updatePubCarInfo(businessPubCarInfo);
                dataFlowContext.addParamOut("carId", businessPubCarInfo.get("carId"));
            }
        }
    }

    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map<String, String> info = new HashMap<>();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map<String, Object> delInfo = new HashMap<>();
        delInfo.put("carBId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        // 车辆信息
        List<Map> pubCarInfo = pubCarServiceDaoImpl.getPubCarInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarInfo)) {

            // 车辆业务表信息
            List<Map> businessPubCarInfos = pubCarServiceDaoImpl.getBusinessPubCarInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCar），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarIndex = 0; pubCarIndex < businessPubCarInfos.size(); pubCarIndex++) {
                Map businessPubCarInfo = businessPubCarInfos.get(pubCarIndex);
                flushBusinessPubCarInfo(businessPubCarInfo, StatusConstant.STATUS_CD_VALID);
                pubCarServiceDaoImpl.updatePubCarInfo(businessPubCarInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR;
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
     * @param business 总的数据节点
     * @param businessPubCar 费用配置节点
     */
    private void doBusinessPubCar(Business business,JSONObject businessPubCar){

        Assert.jsonObjectHaveKey(businessPubCar,"carId","businessPubCar 节点下没有包含 carId 节点");

        if(businessPubCar.getString("carId").startsWith("-")){
            businessPubCar.put("carId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ID));
        }

        autoSaveDelBusinessPubCar(business, businessPubCar);

    }
}

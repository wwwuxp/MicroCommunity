package com.hezong.pubcar.listener.car;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公车 服务侦听 父类
 * Created by hh
 */
public abstract class AbstractPubCarBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractPubCarBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPubCarServiceDao getPubCarServiceDaoImpl();

    /**
     * 刷新 businessFloorInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPubCarInfo
     */
    protected void flushBusinessPubCarInfo(Map businessPubCarInfo, String statusCd) {
        businessPubCarInfo.put("newCarBId", businessPubCarInfo.get("carBId"));
        businessPubCarInfo.put("newAttrBId", businessPubCarInfo.get("attrBId"));
        businessPubCarInfo.put("newInsuranceBId", businessPubCarInfo.get("insuranceBId"));
        businessPubCarInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPubCar 小区公车信息
     */
    protected void autoSaveDelBusinessPubCar(Business business, JSONObject businessPubCar) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("carId", businessPubCar.getString("carId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map> currentPubCarInfos = getPubCarServiceDaoImpl().getPubCarInfo(info);
        if (currentPubCarInfos == null || currentPubCarInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentPubCarInfo = currentPubCarInfos.get(0);

        currentPubCarInfo.put("carBId", business.getbId());
        currentPubCarInfo.put("attrBId", GenerateCodeFactory.getBId());
        currentPubCarInfo.put("insuranceBId", GenerateCodeFactory.getBId());
        currentPubCarInfo.put("operate", StatusConstant.OPERATE_DEL);

        getPubCarServiceDaoImpl().saveDelBusinessPubCarInfo(new JSONObject(currentPubCarInfo));

        for(Object key : currentPubCarInfo.keySet()) {
            if(businessPubCar.get(key) == null) {
                businessPubCar.put(key.toString(), currentPubCarInfo.get(key));
            }
        }
    }


}

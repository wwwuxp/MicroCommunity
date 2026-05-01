package com.hezong.pubcar.listener.driver;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarDriverServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
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
 * 司机 服务侦听 父类
 * Created by hh
 */
public abstract class AbstractPubCarDriverBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractPubCarDriverBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPubCarDriverServiceDao getPubCarDriverServiceDaoImpl();

    /**
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPubCarDriverInfo
     */
    protected void flushBusinessPubCarDriverInfo(Map businessPubCarDriverInfo, String statusCd) {
        businessPubCarDriverInfo.put("newBId", businessPubCarDriverInfo.get("bId"));
        businessPubCarDriverInfo.remove("bId");
        businessPubCarDriverInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPubCarDriver 司机或用车人信息
     */
    protected void autoSaveDelBusinessPubCarDriver(Business business, JSONObject businessPubCarDriver) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("driverId", businessPubCarDriver.getString("driverId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);

        List<Map> currentPubCarDriverInfos = getPubCarDriverServiceDaoImpl().getPubCarDriverInfo(info);
        if (currentPubCarDriverInfos == null || currentPubCarDriverInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentPubCarDriverInfo = currentPubCarDriverInfos.get(0);

        currentPubCarDriverInfo.put("bId", business.getbId());
        currentPubCarDriverInfo.put("operate", StatusConstant.OPERATE_DEL);

        getPubCarDriverServiceDaoImpl().saveBusinessPubCarDriverInfo(new JSONObject(currentPubCarDriverInfo));

        for(Object key : currentPubCarDriverInfo.keySet()) {
            if(businessPubCarDriver.get(key) == null) {
                businessPubCarDriver.put(key.toString(), currentPubCarDriverInfo.get(key));
            }
        }
    }


}

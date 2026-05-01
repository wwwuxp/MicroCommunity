package com.hezong.pubcar.listener.contact;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingPersonnelServiceDao;
import com.hezong.pubcar.dao.IOutGoingUnitContactServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 外出单位联系人 服务侦听 父类
 * Created by hh
 */
public abstract class AbstractOutGoingUnitContactBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractOutGoingUnitContactBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IOutGoingUnitContactServiceDao getOutGoingUnitContactServiceDaoImpl();

    /**
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessOutGoingUnitContactInfo
     */
    protected void flushBusinessOutGoingUnitContactInfo(Map businessOutGoingUnitContactInfo, String statusCd) {
        businessOutGoingUnitContactInfo.put("newBId", businessOutGoingUnitContactInfo.get("bId"));
        businessOutGoingUnitContactInfo.remove("bId");
        businessOutGoingUnitContactInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessOutGoingUnitContact 外出登记信息
     */
    protected void autoSaveDelBusinessOutGoingUnitContact(Business business, JSONObject businessOutGoingUnitContact) {
        //自动插入DEL
//        Map info = new HashMap();
//        info.put("driverId", businessPubCarDriver.getString("driverId"));
//        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
//
//        List<Map> currentPubCarDriverInfos = getPubCarAlarmServiceDaoImpl().getPubCarDriverInfo(info);
//        if (currentPubCarDriverInfos == null || currentPubCarDriverInfos.size() != 1) {
//            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
//        }
//
//        Map currentPubCarDriverInfo = currentPubCarDriverInfos.get(0);
//
//        currentPubCarDriverInfo.put("bId", business.getbId());
//        currentPubCarDriverInfo.put("operate", StatusConstant.OPERATE_DEL);
//
//        getPubCarAlarmServiceDaoImpl().saveBusinessPubCarDriverInfo(new JSONObject(currentPubCarDriverInfo));
//
//        for(Object key : currentPubCarDriverInfo.keySet()) {
//            if(businessPubCarDriver.get(key) == null) {
//                businessPubCarDriver.put(key.toString(), currentPubCarDriverInfo.get(key));
//            }
//        }
    }


}

package com.hezong.pubcar.listener.violation;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarViolationServiceDao;
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
 * 车辆违章信息 服务侦听 父类
 * Created by hh
 */
public abstract class AbstractPubCarViolationBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractPubCarViolationBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPubCarViolationServiceDao getPubCarViolationServiceDaoImpl();

    /**
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPubCarViolationInfo
     */
    protected void flushBusinessPubCarViolationInfo(Map businessPubCarViolationInfo, String statusCd) {
        businessPubCarViolationInfo.put("newBId", businessPubCarViolationInfo.get("bId"));
        businessPubCarViolationInfo.remove("bId");
        businessPubCarViolationInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     */
    protected void autoSaveDelBusinessPubCarDriver(Business business, JSONObject businessPubCarViolation) {
        //自动插入DEL
       Map<String, Object> info = new HashMap<>();
       info.put("violationId", businessPubCarViolation.getString("violationId"));
       info.put("statusCd", StatusConstant.STATUS_CD_VALID);

       List<Map> currentPubCarViolationInfos = getPubCarViolationServiceDaoImpl().getPubCarViolationInfo(info);
       if (currentPubCarViolationInfos == null || currentPubCarViolationInfos.size() != 1) {
           throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
       }

       Map currentPubCarViolationInfo = currentPubCarViolationInfos.get(0);

       currentPubCarViolationInfo.put("bId", business.getbId());
       currentPubCarViolationInfo.put("operate", StatusConstant.OPERATE_DEL);

       getPubCarViolationServiceDaoImpl().saveBusinessPubCarViolationInfo(new JSONObject(currentPubCarViolationInfo));

       for(Object key : currentPubCarViolationInfo.keySet()) {
           if(businessPubCarViolation.get(key) == null) {
               businessPubCarViolation.put(key.toString(), currentPubCarViolationInfo.get(key));
           }
       }
    }


}

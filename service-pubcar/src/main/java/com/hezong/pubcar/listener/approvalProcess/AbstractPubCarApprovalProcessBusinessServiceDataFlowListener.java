package com.hezong.pubcar.listener.approvalProcess;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarApprovalProcessServiceDao;
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
 * 用车审批信息 服务侦听 父类
 * Created by hh
 */
public abstract class AbstractPubCarApprovalProcessBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractPubCarApprovalProcessBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPubCarApprovalProcessServiceDao getPubCarApprovalProcessServiceDaoImpl();

    /**
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPubCarApprovalProcessInfo
     */
    protected void flushBusinessPubCarApprovalProcessInfo(Map businessPubCarApprovalProcessInfo, String statusCd) {
        businessPubCarApprovalProcessInfo.put("newBId", businessPubCarApprovalProcessInfo.get("bId"));
        businessPubCarApprovalProcessInfo.remove("bId");
        businessPubCarApprovalProcessInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPubCarApprovalProcess 司机或用车人信息
     */
    protected void autoSaveDelBusinessPubCarApprovalProcess(Business business, JSONObject businessPubCarApprovalProcess) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("tripId", businessPubCarApprovalProcess.getString("tripId"));
        info.put("reviewerRole", businessPubCarApprovalProcess.getInteger("reviewerRole"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);

        List<Map> currentPubCarApprovalProcessInfos = getPubCarApprovalProcessServiceDaoImpl().getPubCarApprovalProcessInstanceInfo(info);
        if (currentPubCarApprovalProcessInfos == null || currentPubCarApprovalProcessInfos.size() < 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        for (Map currentPubCarApprovalProcessInfo : currentPubCarApprovalProcessInfos) {

            currentPubCarApprovalProcessInfo.put("bId", business.getbId());
            currentPubCarApprovalProcessInfo.put("operate", StatusConstant.OPERATE_DEL);

            getPubCarApprovalProcessServiceDaoImpl().saveBusinessPubCarApprovalProcessInfo(new JSONObject(currentPubCarApprovalProcessInfo));
            for (Object key : currentPubCarApprovalProcessInfo.keySet()) {
                if (businessPubCarApprovalProcess.get(key) == null) {
                    businessPubCarApprovalProcess.put(key.toString(), currentPubCarApprovalProcessInfo.get(key));
                }
            }
        }

    }


}

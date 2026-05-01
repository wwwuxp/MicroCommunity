package com.java110.community.listener.visitor;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitorServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访客邀约人信息 服务侦听 父类
 *
 * @author hh
 * create date 2021/9/16
 */
public abstract class AbstractVisitorBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IVisitorServiceDao getVisitorServiceDaoImpl();

    /**
     * 刷新 businessVisitorInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessVisitorInfo
     */
    protected void flushBusinessVisitorInfo(Map businessVisitorInfo, String statusCd) {
        businessVisitorInfo.put("newBId", businessVisitorInfo.get("bId"));
        businessVisitorInfo.remove("bId");
        businessVisitorInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessVisitor 访客信息信息
     */
    protected void autoSaveDelBusinessVisitor(Business business, JSONObject businessVisitor) {
        //自动插入DEL
        Map<String, Object> info = new HashMap<>();
        info.put("invitationId", businessVisitor.getString("invitationId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map<String, Object>> currentVisitorInfos = getVisitorServiceDaoImpl().getVisitorInfo(info);
        if (CollectionUtils.isEmpty(currentVisitorInfos)) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要删除的数据");
        }
        for (Map<String, Object> currentVisitorInfo : currentVisitorInfos) {
            currentVisitorInfo.put("bId", business.getbId());
            currentVisitorInfo.put("operate", StatusConstant.OPERATE_DEL);
            getVisitorServiceDaoImpl().saveBusinessVisitorInfo(new JSONObject(currentVisitorInfo));

            // for (Object key : currentVisitorInfo.keySet()) {
            //     if (businessVisitor.get(key) == null) {
            //         businessVisitor.put(key.toString(), currentVisitorInfo.get(key));
            //     }
            // }
        }

    }


}

package com.java110.community.listener.visitInvitation;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitInvitationServiceDao;
import com.java110.core.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.entity.center.Business;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访客邀约信息 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractVisitInvitationBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IVisitInvitationServiceDao getVisitInvitationServiceDaoImpl();

    /**
     * 刷新 businessVisitInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessVisitInvitationInfo
     */
    protected void flushBusinessVisitInvitationInfo(Map businessVisitInvitationInfo, String statusCd) {
        businessVisitInvitationInfo.put("newBId", businessVisitInvitationInfo.get("bId"));
        businessVisitInvitationInfo.remove("bId");
        businessVisitInvitationInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessVisitInvitation 访客信息信息
     */
    protected void autoSaveDelBusinessVisitInvitation(Business business, JSONObject businessVisitInvitation) {
        //自动插入DEL
        Map<String, String> info = new HashMap<>();
        info.put("invitationId", businessVisitInvitation.getString("invitationId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        List<Map<String, Object>> currentVisitInvitationInfos = getVisitInvitationServiceDaoImpl().getVisitInvitationInfo(info);
        if (currentVisitInvitationInfos == null || currentVisitInvitationInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map<String, Object> currentVisitInvitationInfo = currentVisitInvitationInfos.get(0);

        currentVisitInvitationInfo.put("bId", business.getbId());
        currentVisitInvitationInfo.put("operate", StatusConstant.OPERATE_DEL);
        getVisitInvitationServiceDaoImpl().saveBusinessVisitInvitationInfo(new JSONObject(currentVisitInvitationInfo));

        for (Object key : currentVisitInvitationInfo.keySet()) {
            if (businessVisitInvitation.get(key) == null) {
                businessVisitInvitation.put(key.toString(), currentVisitInvitationInfo.get(key));
            }
        }
    }


}

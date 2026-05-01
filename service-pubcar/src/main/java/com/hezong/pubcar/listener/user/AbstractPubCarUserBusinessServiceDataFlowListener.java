package com.hezong.pubcar.listener.user;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarUserServiceDao;
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
 * 用车人 服务侦听 父类
 * Created by hh
 */
public abstract class AbstractPubCarUserBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractPubCarUserBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPubCarUserServiceDao getPubCarUserServiceDaoImpl();

    /**
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPubCarUserInfo
     */
    protected void flushBusinessPubCarUserInfo(Map businessPubCarUserInfo, String statusCd) {
        businessPubCarUserInfo.put("newBId", businessPubCarUserInfo.get("bId"));
        businessPubCarUserInfo.remove("bId");
        businessPubCarUserInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPubCarUser 用车人信息
     */
    protected void autoSaveDelBusinessPubCarUser(Business business, JSONObject businessPubCarUser) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("carUserId", businessPubCarUser.getString("carUserId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);

        List<Map> currentPubCarUserInfos = getPubCarUserServiceDaoImpl().getPubCarUserInfo(info);
        if (currentPubCarUserInfos == null || currentPubCarUserInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentPubCarUserInfo = currentPubCarUserInfos.get(0);

        currentPubCarUserInfo.put("bId", business.getbId());
        currentPubCarUserInfo.put("operate", StatusConstant.OPERATE_DEL);

        getPubCarUserServiceDaoImpl().saveBusinessPubCarUserInfo(new JSONObject(currentPubCarUserInfo));

        for(Object key : currentPubCarUserInfo.keySet()) {
            if(businessPubCarUser.get(key) == null) {
                businessPubCarUser.put(key.toString(), currentPubCarUserInfo.get(key));
            }
        }
    }


}

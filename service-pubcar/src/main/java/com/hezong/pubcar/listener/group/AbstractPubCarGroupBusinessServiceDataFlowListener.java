package com.hezong.pubcar.listener.group;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarGroupServiceDao;
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
 * 分组 服务侦听 父类
 * Created by hh
 */
public abstract class AbstractPubCarGroupBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(AbstractPubCarGroupBusinessServiceDataFlowListener.class);

    /**
     * 获取 DAO工具类
     *
     * @return
     */
    public abstract IPubCarGroupServiceDao getPubCarGroupServiceDaoImpl();

    /**
     * 主要将 数据库 中字段和 接口传递字段建立关系
     *
     * @param businessPubCarGroupInfo
     */
    protected void flushBusinessPubCarGroupInfo(Map businessPubCarGroupInfo, String statusCd) {
        businessPubCarGroupInfo.put("newBId", businessPubCarGroupInfo.get("bId"));
        businessPubCarGroupInfo.remove("bId");
        businessPubCarGroupInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     *
     * @param businessPubCarGroup 分组信息
     */
    protected void autoSaveDelBusinessPubCarGroup(Business business, JSONObject businessPubCarGroup) {
        //自动插入DEL
        Map info = new HashMap();
        info.put("groupId", businessPubCarGroup.getString("groupId"));
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);

        List<Map> currentPubCarGroupInfos = getPubCarGroupServiceDaoImpl().getPubCarGroupInfo(info);
        if (currentPubCarGroupInfos == null || currentPubCarGroupInfos.size() != 1) {
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR, "未找到需要修改数据信息，入参错误或数据有问题，请检查" + info);
        }

        Map currentPubCarGroupInfo = currentPubCarGroupInfos.get(0);

        currentPubCarGroupInfo.put("bId", business.getbId());
        currentPubCarGroupInfo.put("operate", StatusConstant.OPERATE_DEL);

        getPubCarGroupServiceDaoImpl().saveBusinessPubCarGroupInfo(new JSONObject(currentPubCarGroupInfo));

        for(Object key : currentPubCarGroupInfo.keySet()) {
            if(businessPubCarGroup.get(key) == null) {
                businessPubCarGroup.put(key.toString(), currentPubCarGroupInfo.get(key));
            }
        }
    }


}

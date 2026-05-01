package com.hezong.pubcar.listener.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarUserServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarUser.PubCarUserPo;
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
 * 删除用车人或自驾人信息 侦听
 * @author hh
 * @datetime 2021/7/24 10:48
 */
@Java110Listener("deletePubCarUserInfoListener")
@Transactional
public class DeletePubCarUserInfoListener extends AbstractPubCarUserBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarUserServiceDao pubCarUserServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarUserPo.class.getSimpleName())){
            Object obj = data.get(PubCarUserPo.class.getSimpleName());
            JSONArray businessPubCarUsers = null;
            if(obj instanceof JSONObject){
                businessPubCarUsers = new JSONArray();
                businessPubCarUsers.add(obj);
            }else {
                businessPubCarUsers = (JSONArray)obj;
            }
            for (int pubCarUserIndex = 0; pubCarUserIndex < businessPubCarUsers.size();pubCarUserIndex++) {
                JSONObject businessPubCarUser = businessPubCarUsers.getJSONObject(pubCarUserIndex);
                doBusinessPubCarUser(business, businessPubCarUser);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("carUserId", businessPubCarUser.getString("carUserId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);


        List<Map> businessPubCarUserInfos = pubCarUserServiceDaoImpl.getBusinessPubCarUserInfo(info);
        if (businessPubCarUserInfos != null && businessPubCarUserInfos.size() > 0) {
            for (int pubCarDriverIndex = 0; pubCarDriverIndex < businessPubCarUserInfos.size(); pubCarDriverIndex++) {

                Map businessPubCarUserInfo = businessPubCarUserInfos.get(pubCarDriverIndex);
                flushBusinessPubCarUserInfo(businessPubCarUserInfo, StatusConstant.STATUS_CD_INVALID);

                pubCarUserServiceDaoImpl.updatePubCarUserInfo(businessPubCarUserInfo);
                if (businessPubCarUserInfo.size() == 1) {
                    dataFlowContext.addParamOut("carUserId", businessPubCarUserInfo.get("carUserId"));
                }
            }
        }
    }

    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        Map<String, String> info = new HashMap<>();
        info.put("bId", bId);
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        Map<String, String> delInfo = new HashMap<>();
        delInfo.put("bId", business.getbId());
        delInfo.put("operate", StatusConstant.OPERATE_DEL);
        // 用车人或自驾人信息
        List<Map> pubCarUserInfo = pubCarUserServiceDaoImpl.getPubCarUserInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarUserInfo)) {

            // 用车人或自驾人信息
            List<Map> businessPubCarUserInfos = pubCarUserServiceDaoImpl.getBusinessPubCarUserInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarUserInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarUser），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarUserIndex = 0; pubCarUserIndex < businessPubCarUserInfos.size(); pubCarUserIndex++) {
                Map businessPubCarUserInfo = businessPubCarUserInfos.get(pubCarUserIndex);
                flushBusinessPubCarUserInfo(businessPubCarUserInfo, StatusConstant.STATUS_CD_VALID);
                pubCarUserServiceDaoImpl.updatePubCarUserInfo(businessPubCarUserInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_USER;
    }

    @Override
    public IPubCarUserServiceDao getPubCarUserServiceDaoImpl() {
        return pubCarUserServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarUser
     */
    private void doBusinessPubCarUser(Business business, JSONObject businessPubCarUser){

        Assert.jsonObjectHaveKey(businessPubCarUser,"carUserId","businessPubCarUser 节点下没有包含 carUserId 节点");

        if(businessPubCarUser.getString("carUserId").startsWith("-")){
            businessPubCarUser.put("carUserId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_CAR_USER_ID));
        }

        autoSaveDelBusinessPubCarUser(business, businessPubCarUser);

    }
}

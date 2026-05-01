package com.hezong.pubcar.listener.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarGroupServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarGroup.PubCarGroupPo;
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
 * 删除分组信息
 * @author hh
 * @datetime 2021/8/10 17:37
 */
@Java110Listener("deletePubCarGroupInfoListener")
@Transactional
public class DeletePubCarGroupInfoListener extends AbstractPubCarGroupBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarGroupServiceDao pubCarGroupServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        if(data.containsKey(PubCarGroupPo.class.getSimpleName())){
            Object obj = data.get(PubCarGroupPo.class.getSimpleName());
            JSONArray businessPubCarGroups = null;
            if(obj instanceof JSONObject){
                businessPubCarGroups = new JSONArray();
                businessPubCarGroups.add(obj);
            }else {
                businessPubCarGroups = (JSONArray)obj;
            }
            for (int pubCarGroupIndex = 0; pubCarGroupIndex < businessPubCarGroups.size();pubCarGroupIndex++) {
                JSONObject businessPubCarGroup = businessPubCarGroups.getJSONObject(pubCarGroupIndex);
                doBusinessPubCarGroup(business, businessPubCarGroup);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("groupId", businessPubCarGroup.getString("groupId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId", business.getbId());
        info.put("operate", StatusConstant.OPERATE_DEL);


        List<Map> businessPubCarGroupInfos = pubCarGroupServiceDaoImpl.getBusinessPubCarGroupInfo(info);
        if (businessPubCarGroupInfos != null && businessPubCarGroupInfos.size() > 0) {
            for (int pubCarGroupIndex = 0; pubCarGroupIndex < businessPubCarGroupInfos.size(); pubCarGroupIndex++) {

                Map businessPubCarGroupInfo = businessPubCarGroupInfos.get(pubCarGroupIndex);
                flushBusinessPubCarGroupInfo(businessPubCarGroupInfo, StatusConstant.STATUS_CD_INVALID);

                pubCarGroupServiceDaoImpl.updatePubCarGroupInfo(businessPubCarGroupInfo);
                if (businessPubCarGroupInfo.size() == 1) {
                    dataFlowContext.addParamOut("groupId", businessPubCarGroupInfo.get("groupId"));
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
        // 分组信息
        List<Map> pubCarGroupInfo = pubCarGroupServiceDaoImpl.getPubCarGroupInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarGroupInfo)) {

            // 分组信息
            List<Map> businessPubCarGroupInfos = pubCarGroupServiceDaoImpl.getBusinessPubCarGroupInfo(delInfo);
            // 除非程序出错了，这里不会为空
            if (CollectionUtils.isEmpty(businessPubCarGroupInfos)) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "撤单失败（pubCarGroup），程序内部异常,请检查！ " + delInfo);
            }
            for (int pubCarGroupIndex = 0; pubCarGroupIndex < businessPubCarGroupInfos.size(); pubCarGroupIndex++) {
                Map businessPubCarGroupInfo = businessPubCarGroupInfos.get(pubCarGroupIndex);
                flushBusinessPubCarGroupInfo(businessPubCarGroupInfo, StatusConstant.STATUS_CD_VALID);
                pubCarGroupServiceDaoImpl.updatePubCarGroupInfo(businessPubCarGroupInfo);
            }
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_PUBCAR_GROUP;
    }

    @Override
    public IPubCarGroupServiceDao getPubCarGroupServiceDaoImpl() {
        return pubCarGroupServiceDaoImpl;
    }

    /**
     * 处理 businessPubCar 节点
     * @param business
     * @param businessPubCarGroup
     */
    private void doBusinessPubCarGroup(Business business, JSONObject businessPubCarGroup){

        Assert.jsonObjectHaveKey(businessPubCarGroup,"groupId","businessPubCarGroup 节点下没有包含 groupId 节点");

        if(businessPubCarGroup.getString("groupId").startsWith("-")){
            businessPubCarGroup.put("groupId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_GROUP_ID));
        }

        autoSaveDelBusinessPubCarGroup(business, businessPubCarGroup);

    }
}

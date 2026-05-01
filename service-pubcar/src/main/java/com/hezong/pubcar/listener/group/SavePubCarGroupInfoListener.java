package com.hezong.pubcar.listener.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarAlarmServiceDao;
import com.hezong.pubcar.dao.IPubCarGroupServiceDao;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.po.pubCarGroup.PubCarGroupPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存分组信息 侦听
 * @author hh
 * @datetime 2021/8/10 17:37
 */
@Java110Listener("savePubCarGroupInfoListener")
@Transactional
public class SavePubCarGroupInfoListener extends AbstractPubCarGroupBusinessServiceDataFlowListener {

    @Autowired
    private IPubCarGroupServiceDao pubCarGroupServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
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
                doBusinessGroup(business, businessPubCarGroup);
                if(obj instanceof JSONObject) {
                    dataFlowContext.addParamOut("groupId", businessPubCarGroup.getString("groupId"));
                }
            }
        }
    }

    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        // 分组信息
        List<Map> businessPubCarGroupInfo = pubCarGroupServiceDaoImpl.getBusinessPubCarGroupInfo(info);
        if( businessPubCarGroupInfo != null && businessPubCarGroupInfo.size() >0) {
            pubCarGroupServiceDaoImpl.savePubCarGroupInfo(info);
            if(businessPubCarGroupInfo.size() == 1) {
                dataFlowContext.addParamOut("groupId", businessPubCarGroupInfo.get(0).get("groupId"));
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
        info.put("statusCd", StatusConstant.STATUS_CD_VALID);
        Map<String, String> paramIn = new HashMap<>();
        paramIn.put("bId", bId);
        paramIn.put("statusCd", StatusConstant.STATUS_CD_INVALID);
        // 分组信息
        List<Map> pubCarGroupInfo = pubCarGroupServiceDaoImpl.getPubCarGroupInfo(info);
        if (CollectionUtils.isNotEmpty(pubCarGroupInfo)) {
            pubCarGroupServiceDaoImpl.updatePubCarGroupInfo(paramIn);
        }
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_PUBCAR_GROUP;
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
    private void doBusinessGroup(Business business, JSONObject businessPubCarGroup){

        Assert.jsonObjectHaveKey(businessPubCarGroup,"groupId","businessPubCarGroup 节点下没有包含 groupId 节点");

        if(businessPubCarGroup.getString("groupId").startsWith("-")){
            businessPubCarGroup.put("groupId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_GROUP_ID));
        }

        businessPubCarGroup.put("bId",business.getbId());
        businessPubCarGroup.put("operate", StatusConstant.OPERATE_ADD);
        // 保存分组信息
        pubCarGroupServiceDaoImpl.saveBusinessPubCarGroupInfo(businessPubCarGroup);

    }
}

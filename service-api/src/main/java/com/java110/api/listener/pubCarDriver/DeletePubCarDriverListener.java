package com.java110.api.listener.pubCarDriver;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarDriver.IPubCarDriverBMO;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;

import com.java110.core.annotation.Java110Listener;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.AppService;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceCodePubCarDriverConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

/**
 * 删除司机侦听
 * add by hh
 */
@Java110Listener("deletePubCarDriverListener")
public class DeletePubCarDriverListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarDriverBMO pubCarDriverBMOImpl;

    @Autowired
    private IUserBMO userBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        pubCarDriverBMOImpl.deletePubCarDriver(reqJson, context);

        // 删除掉同步在u_user、s_store_user表的数据
        userBMOImpl.deleteStaff(reqJson, context);

        //删除用户
        userBMOImpl.deleteUser(reqJson, context);

        // 删除掉用户的权限
        deleteUserPrivilege(context, reqJson);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarDriverConstant.DELETE_PUBCAR_DRIVER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    /**
     * 删除用户权限
     *
     * @param dataFlowContext
     * @param paramInJson
     */
    private void deleteUserPrivilege(DataFlowContext dataFlowContext, JSONObject paramInJson) {

        ResponseEntity responseEntity;
        AppService appService = DataFlowFactory.getService(dataFlowContext.getAppId(), ServiceCodeConstant.SERVICE_CODE_DELETE_USER_ALL_PRIVILEGE);
        if (appService == null) {
            responseEntity = new ResponseEntity<>("当前没有权限访问" + ServiceCodeConstant.SERVICE_CODE_DELETE_USER_ALL_PRIVILEGE, HttpStatus.UNAUTHORIZED);
            dataFlowContext.setResponseEntity(responseEntity);
            return;
        }
        HttpHeaders header = new HttpHeaders();
        header.add(CommonConstant.HTTP_SERVICE.toLowerCase(), ServiceCodeConstant.SERVICE_CODE_DELETE_USER_ALL_PRIVILEGE);
        userBMOImpl.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());
        JSONObject paramInObj = new JSONObject();
        paramInObj.put("userId", paramInJson.getString("userId"));
        HttpEntity<String> httpEntity = new HttpEntity<String>(paramInObj.toJSONString(), header);
        doRequest(dataFlowContext, appService, httpEntity);
    }

}

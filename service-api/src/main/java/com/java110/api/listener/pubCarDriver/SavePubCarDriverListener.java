package com.java110.api.listener.pubCarDriver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarDriver.IPubCarDriverBMO;
import com.java110.api.bmo.user.IUserBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.dto.user.UserDto;
import com.java110.intf.order.IPrivilegeInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ServiceCodePubCarDriverConstant;
import com.java110.utils.constant.StoreUserRelConstant;
import com.java110.utils.util.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

/**
 * 保存司机侦听
 * add by hh
 */
@Java110Listener("savePubCarDriverListener")
public class SavePubCarDriverListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarDriverBMO pubCarDriverBMOImpl;
    @Autowired
    private IUserBMO userBMOImpl;
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {
        // 保存公车司机信息
        pubCarDriverBMOImpl.addPubCarDriver(reqJson, context);

        // 判断是否已经有该用户了(姓名，电话，工号都相同，视为一个用户)
        UserDto userDto = new UserDto();
        userDto.setName(reqJson.getString("name"));
        userDto.setJobNum(reqJson.getString("jobNum"));
        userDto.setTel(reqJson.getString("tel"));
        if (userInnerServiceSMOImpl.hasUser(userDto)){
            return;
        }
        // 司机需要登陆移动端承接行程，将司机信息同步到u_user表
        JSONObject userJson = new JSONObject();
        userJson.put("userId", reqJson.getString("driverId"));
        userJson.put("name", reqJson.getString("name"));

        String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        Assert.hasLength(staffDefaultPassword, "映射表中未设置默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        staffDefaultPassword = AuthenticationFactory.passwdMd5(staffDefaultPassword);

        userJson.put("password", staffDefaultPassword);
        userJson.put("orgId", reqJson.getString("departmentId"));
        userJson.put("age", "-1");
        userJson.put("sex", reqJson.getString("gender"));
        userJson.put("tel", reqJson.getString("tel"));
        userJson.put("jobNum", reqJson.getString("jobNum"));
        userBMOImpl.addUser(userJson, context);

        // 添加数据到s_store_user表
        userJson.put("relCd", StoreUserRelConstant.REL_COMMON);
        userJson.put("storeId", reqJson.getString("storeId"));
        userBMOImpl.addStaff(userJson, context);

        // 添加数据到u_org_staff_rel表
        // 这里的relCd是职位代号
        userJson.put("relCd", reqJson.getString("positionId"));
        userBMOImpl.addStaffOrg(userJson, context);

        // 为添加的司机赋权限
        JSONObject privilegeJson = new JSONObject();
        privilegeJson.put("storeTypeCd", reqJson.getString("storeTypeCd"));
        privilegeJson.put("privilegeFlag", "0");
        privilegeJson.put("pFlag", 2);
        privilegeJson.put("domain", reqJson.getString("storeTypeCd"));
        privilegeJson.put("userId", reqJson.getString("driverId"));
        JSONArray pIdArray = new JSONArray();
        // 502021090189610033:对应司机行程的p_id
        pIdArray.add(new JSONObject().put("pId", "502021090189610033"));
        privilegeJson.put("pIds", pIdArray);
        privilegeJson.put("pId", "502021090189610033");
        privilegeJson.put("storeId", reqJson.getString("storeId"));

        List<Map> privilegeGroups = privilegeInnerServiceSMOImpl.queryUserPrivilege(privilegeJson);
        if (CollectionUtils.isNotEmpty(privilegeGroups)){
            return;
        }
        privilegeInnerServiceSMOImpl.addUserPrivilege(privilegeJson);

    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarDriverConstant.ADD_PUBCAR_DRIVER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}

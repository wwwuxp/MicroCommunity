package com.java110.api.listener.pubCarUser;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarUser.DepartmentStatisticsDto;
import com.java110.dto.pubCarUser.PubCarUserDto;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarUserConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ConstantUtil;
import com.java110.vo.api.pubCarUser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 查询用车人或自驾侦听类
 */
@Java110Listener("listPubCarUsersListener")
public class ListPubCarUsersListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarUserInnerServiceSMO pubCarUserInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarUserConstant.LIST_PUBCARUSERS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        if (reqJson.containsKey(ConstantUtil.SIMPLE_FLAG)) {
            String simpleFlag = reqJson.getString(ConstantUtil.SIMPLE_FLAG);
            if (ConstantUtil.SIMPLE_FLAG_DEPARTMENT_NAME.equals(simpleFlag)) {
                Assert.hasKeyAndValue(reqJson, "storeId", "请求中未包含storeId节点");
            }
        }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        if (reqJson.containsKey(ConstantUtil.SIMPLE_FLAG)) {
            String simpleFlag = reqJson.getString(ConstantUtil.SIMPLE_FLAG);

            // 获取用车人的姓名，性别，部门，职位等详细时间
            if (ConstantUtil.SIMPLE_FLAG_YES.equals(simpleFlag)) {
                getCarUserDetailInfo(context, reqJson);
                return;
            }

            // 获取所有部门下用车统计信息
            if (ConstantUtil.SIMPLE_FLAG_STATICS.equals(simpleFlag)) {
                getDepartmentStatistics(context, reqJson);
                return;
            }

            // 获取所有部门名字
            if (ConstantUtil.SIMPLE_FLAG_DEPARTMENT_NAME.equals(simpleFlag)) {
                getDepartmentName(context, reqJson);
                return;
            }
        }


        // 获取的用车人信息只有id，没有详细信息
        PubCarUserDto pubCarUserDto = BeanConvertUtil.covertBean(reqJson, PubCarUserDto.class);

        int count = pubCarUserInnerServiceSMOImpl.queryPubCarUsersCount(pubCarUserDto);

        List<ApiPubCarUserDataVo> pubCarUsers;

        if (count > 0) {
            pubCarUsers = BeanConvertUtil.covertBeanList(pubCarUserInnerServiceSMOImpl.queryPubCarUsers(pubCarUserDto), ApiPubCarUserDataVo.class);
        } else {
            pubCarUsers = Collections.emptyList();
        }

        ApiPubCarUserVo apiPubCarUserVo = new ApiPubCarUserVo();

        apiPubCarUserVo.setTotal(count);
        apiPubCarUserVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarUserVo.setData(pubCarUsers);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPubCarUserVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 只获取部门名字
     *
     * @param context
     * @param reqJson
     */
    private void getDepartmentName(DataFlowContext context, JSONObject reqJson) {
        String storeId = reqJson.getString("storeId");

        List<ApiDepartmentNamesDataVo> departmentNames = pubCarUserInnerServiceSMOImpl.queryDepartmentNames(storeId);

        ApiDepartmentNamesVo apiDepartmentNamesVo = new ApiDepartmentNamesVo();
        apiDepartmentNamesVo.setData(departmentNames);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiDepartmentNamesVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 获取部门用车统计信息
     *
     * @param context
     * @param reqJson
     */
    private void getDepartmentStatistics(DataFlowContext context, JSONObject reqJson) {
        DepartmentStatisticsDto departmentStatisticsDto = BeanConvertUtil.covertBean(reqJson, DepartmentStatisticsDto.class);

        int count = pubCarUserInnerServiceSMOImpl.queryDepartmentStatisticsCount(departmentStatisticsDto);

        List<ApiDepartmentStatisticsDataVo> departmentStatistics;

        if (count > 0) {
            departmentStatistics = pubCarUserInnerServiceSMOImpl.queryDepartmentStatistics(departmentStatisticsDto);
        } else {
            departmentStatistics = Collections.emptyList();
        }

        // 处理为null的数据
        for (ApiDepartmentStatisticsDataVo departmentStatistic : departmentStatistics) {
            if (departmentStatistic.getMileage() == null) {
                departmentStatistic.setMileage((double) 0);
            }
            if (departmentStatistic.getDuration() == null) {
                departmentStatistic.setDuration(0);
            }
        }

        ApiDepartmentStatisticsVo apiDepartmentStatisticsVo = new ApiDepartmentStatisticsVo();

        apiDepartmentStatisticsVo.setTotal(count);
        apiDepartmentStatisticsVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiDepartmentStatisticsVo.setData(departmentStatistics);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiDepartmentStatisticsVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 获取用车人的详细信息
     *
     * @param context
     * @param reqJson
     */
    private void getCarUserDetailInfo(DataFlowContext context, JSONObject reqJson) {
        PubCarUserDto pubCarUserDto = BeanConvertUtil.covertBean(reqJson, PubCarUserDto.class);

        int count = pubCarUserInnerServiceSMOImpl.queryPubCarUserDetailsCount(pubCarUserDto);

        List<ApiPubCarUserDetailDataVo> pubCarUserDetails;

        if (count > 0) {
            pubCarUserDetails = pubCarUserInnerServiceSMOImpl.queryPubCarUserDetails(pubCarUserDto);
        } else {
            pubCarUserDetails = Collections.emptyList();
        }

        ApiPubCarUserDetailVo apiPubCarUserDetailVo = new ApiPubCarUserDetailVo();

        apiPubCarUserDetailVo.setTotal(count);
        if (Objects.isNull(reqJson.getInteger("row"))){
            apiPubCarUserDetailVo.setRecords(1);
        }else {
            apiPubCarUserDetailVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        }
        apiPubCarUserDetailVo.setData(pubCarUserDetails);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarUserDetailVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}

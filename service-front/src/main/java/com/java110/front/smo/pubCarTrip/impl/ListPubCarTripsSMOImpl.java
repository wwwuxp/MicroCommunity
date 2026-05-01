package com.java110.front.smo.pubCarTrip.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.pubCarTrip.IListPubCarTripsSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询pubCarTrip服务类
 */
@Service("listPubCarTripsSMOImpl")
public class ListPubCarTripsSMOImpl extends AbstractComponentSMO implements IListPubCarTripsSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listPubCarTrips(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    public ResponseEntity<String> choosePubCarTrip(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");

        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_TRIP_CHOOSE);

        return commonResponse(pd, paramIn);
    }

    @Override
    public ResponseEntity<String> listCarTripStatistics(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "carId", "请求中未包含车辆id节点");
        super.validatePageInfo(pd);

        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_STATICS_CAR);

        return commonResponse(pd, paramIn);
    }

    @Override
    public ResponseEntity<String> listDepartmentTripStatistics(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "departmentId", "请求中未包含部门id节点");
        super.validatePageInfo(pd);

        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_STATICS);

        return commonResponse(pd, paramIn);
    }

    @Override
    public ResponseEntity<String> listDriverTripStatistics(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "driverId", "请求中未包含司机id节点");
        super.validatePageInfo(pd);

        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_STATICS);

        return commonResponse(pd, paramIn);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        super.validatePageInfo(pd);
    }

    @Override
    public ResponseEntity<String> listDriverTrips(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "driverId", "请求中未包含司机id节点");

        paramIn.put(ConstantUtil.SIMPLE_FLAG, ConstantUtil.SIMPLE_FLAG_YES);

        return commonResponse(pd, paramIn);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        return commonResponse(pd, paramIn);
    }

    /**
     * 远程调用公共方法
     *
     * @param pd
     * @param paramIn
     * @return
     */
    private ResponseEntity<String> commonResponse(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        paramIn.put("storeId", result.getStoreId());
        paramIn.put("storeTypeCd", result.getStoreTypeCd());

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/pubCarTrip.listPubCarTrips" + mapToUrlParam(paramIn);

        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}

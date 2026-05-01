package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListAttendanceFileSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 查询导入文件的列表
 * @author hh
 * @date 2021/9/27
 */
@Service("listAttendanceFileSMOImpl")
public class ListAttendanceFileSMOImpl extends AbstractComponentSMO implements IListAttendanceFileSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/attendanceFile.listAttendanceFiles" + mapToUrlParam(paramIn);

        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> listAttendanceFile(IPageData pd) {
        return businessProcess(pd);
    }
}

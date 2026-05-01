package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IListAttendanceNotWorkDateSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author lmf
 * @date 2022/6/29
 */
@Service("listAttendanceNotWorkDateSMOImpl")
public class ListAttendanceNotWorkDateSMOImpl extends AbstractComponentSMO implements IListAttendanceNotWorkDateSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        super.validatePageInfo(pd);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/attendanceNotWorkDate.listAttendanceNotWorkDate" + mapToUrlParam(paramIn);
        return this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);
    }

    @Override
    public ResponseEntity<String> listAttendanceNotWorkDate(IPageData pd) {
        return businessProcess(pd);
    }
}

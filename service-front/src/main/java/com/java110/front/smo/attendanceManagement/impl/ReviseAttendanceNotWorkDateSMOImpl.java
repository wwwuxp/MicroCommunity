package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IReviseAttendanceNotWorkDateSMO;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author lmf
 * @date 2022/6/30
 */
@Service("reviseAttendanceNotWorkDateSMOImpl")
public class ReviseAttendanceNotWorkDateSMOImpl extends AbstractComponentSMO implements IReviseAttendanceNotWorkDateSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        String componentMethod = pd.getComponentMethod();
        String apiUrl = ServiceConstant.SERVICE_API_URL;
        if("saveOrEdit".equals(componentMethod)){
            apiUrl += "/api/attendanceNotWorkDate.saveOrEdit";
        }else if("delete".equals(componentMethod)){
            apiUrl += "/api/attendanceNotWorkDate.delete";
        }
        return this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                apiUrl,
                HttpMethod.POST);
    }

    @Override
    public ResponseEntity<String> saveOrEdit(IPageData pd) {
        ResponseEntity<String> stringResponseEntity = businessProcess(pd);
        return stringResponseEntity;
    }

    @Override
    public ResponseEntity<String> delete(IPageData pd) {
        return businessProcess(pd);
    }

}

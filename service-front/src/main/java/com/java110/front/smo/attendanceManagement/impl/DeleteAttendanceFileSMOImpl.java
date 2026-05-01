package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.front.smo.attendanceManagement.IDeleteAttendanceFileSMO;
import com.java110.intf.community.IAttendanceFileInnerServiceSMO;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hh
 * @date 2021/9/27
 */
@Service("deleteAttendanceFileSMOImpl")
public class DeleteAttendanceFileSMOImpl extends AbstractComponentSMO implements IDeleteAttendanceFileSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IAttendanceFileInnerServiceSMO attendanceFileInnerServiceSMOImpl;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "dataType", "文件数据类型参数不能为空");
        Assert.hasKeyAndValue(paramIn, "id", "文件id参数不能为空");
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        Integer id = paramIn.getInteger("id");
        Integer dataType = paramIn.getInteger("dataType");

        try {
            attendanceFileInnerServiceSMOImpl.deleteAttendanceFileById(id, dataType);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.contains("请求参数错误") && msg.contains("{") && msg.contains("}")) {
                msg = msg.substring(msg.indexOf("{"));
            }
            JSONObject msgJson = JSONObject.parseObject(msg);
            throw new SMOException(msgJson.getString("message"));
        }

        return ResultVo.success();
    }

    @Override
    public ResponseEntity<String> deleteAttendanceFile(IPageData pd) {
        return businessProcess(pd);
    }

}

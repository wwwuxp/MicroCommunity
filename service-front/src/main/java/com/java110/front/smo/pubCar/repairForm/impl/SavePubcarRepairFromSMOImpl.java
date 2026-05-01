package com.java110.front.smo.pubCar.repairForm.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.AbstractComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.pubcar.BusinessPubcarRepairForm;
import com.java110.front.smo.pubCar.repairForm.CachePubcarRepairFromSMO;
import com.java110.front.smo.pubCar.repairForm.SavePubcarRepairFromSMO;
import com.java110.intf.pubcar.PubcarRepairFormSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.ServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lmf
 * @date 2022/7/26
 */
@Service("savePubcarRepairFromSMOImpl")
public class SavePubcarRepairFromSMOImpl extends AbstractComponentSMO implements SavePubcarRepairFromSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    PubcarRepairFormSMO pubcarRepairFormSMO;

    // @Autowired
    // FtpFileSMO ftpFileSMO;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> saveOrEdit(IPageData pd, MultipartFile uploadFile) {
        //获取参数
        String reqDataString = pd.getReqData();
        BusinessPubcarRepairForm pubcarRepairForm = JSONObject.parseObject(reqDataString, BusinessPubcarRepairForm.class);
        pubcarRepairForm.setUserId(pd.getUserId());
        pubcarRepairForm.setUserName(pd.getUserName());
        // 2022-10-20 需求变更 需要上传多个文件 因此先上传再保存文件信息
        //数据校验,查看是否有添加附件，如果存在则添加文件信息
        // if(uploadFile != null){
        //     try{
        //         ResponseEntity<String> ftpFileResult = ftpFileSMO.saveFile(pd, uploadFile);
        //         String body = ftpFileResult.getBody();
        //         FtpFile ftpFile = JSONObject.parseObject(body, FtpFile.class);
        //         pubcarRepairForm.setFileSize(ftpFile.getFileSize());
        //         pubcarRepairForm.setFileName(ftpFile.getFileName());
        //         pubcarRepairForm.setFtpId(ftpFile.getFtpId());
        //     }catch (Exception e){
        //         e.printStackTrace();
        //     }
        // }
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(pd));
        jsonObject.put("reqData", JSONObject.toJSONString(pubcarRepairForm));
        return super.businessProcess(jsonObject.toJavaObject(IPageData.class));
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception {
        ResponseEntity<String> responseEntity = null;
        super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/pubcarRepairFrom.saveOrEdit",
                HttpMethod.POST);
        return responseEntity;
    }
}

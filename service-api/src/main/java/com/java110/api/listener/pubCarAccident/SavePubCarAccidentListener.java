package com.java110.api.listener.pubCarAccident;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarAccident.IPubCarAccidentBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodePubCarAccidentConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;
import java.util.StringJoiner;

/**
 * 保存公车用车事故信息 侦听
 * @author hh
 * @date 2021/10/9
 */
@Java110Listener("savePubCarAccidentListener")
public class SavePubCarAccidentListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IPubCarAccidentBMO pubCarAccidentBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "tripId", "请选择行程");
        // Assert.hasKeyAndValue(paramIn, "accidentSceneImg", "事故现场照片不能为空");
        // Assert.hasKeyAndValue(paramIn, "responsibilityConfirmImg", "责任认定书照片不能为空");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {

        reqJson.put("accidentId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ACCIDENT_ID));

        JSONArray businessJsonArray = new JSONArray();
        // 保存事故现场照片
        String accidentSceneImg = saveFile(reqJson, businessJsonArray, "accidentSceneImg");
        if (StringUtils.isNotBlank(accidentSceneImg)){
            reqJson.put("accidentSceneImg", accidentSceneImg);
        }else {
            reqJson.put("accidentSceneImg", null);
        }

        // 保存责任认定书照片
        String responsibilityConfirmImg = saveFile(reqJson, businessJsonArray, "responsibilityConfirmImg");
        if (StringUtils.isNotBlank(responsibilityConfirmImg)){
            reqJson.put("responsibilityConfirmImg", responsibilityConfirmImg);
        }else {
            reqJson.put("responsibilityConfirmImg", null);
        }

        if (CollectionUtils.isNotEmpty(businessJsonArray)){
            handleBusinessJsonArrayData(context, businessJsonArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL, FileRelPo.class);
        }

        // 保存用车事故信息
        pubCarAccidentBMOImpl.addPubCarAccident(reqJson, context);

    }

    /**
     * 保存图片文件
     * @param reqJson 请求参数
     * @param businessJsonArray businessJsonArray
     * @param imgParam 图片属性名
     * @return 图片id组成的字符串，中间以逗号相隔
     */
    private String saveFile(JSONObject reqJson, JSONArray businessJsonArray, String imgParam) {
        if (reqJson.containsKey(imgParam) && CollectionUtils.isNotEmpty(reqJson.getJSONArray(imgParam))) {
            StringJoiner sj = new StringJoiner(",");
            JSONArray imgArray = reqJson.getJSONArray(imgParam);

            for (int i = 0, size = imgArray.size(); i < size; i++) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(imgArray.getString(i));
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

                sj.add(fileDto.getFileId());
                reqJson.put("fileSaveName", fileName);

                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId("-1");
                fileRelPo.setFileRealName(fileDto.getFileId());
                fileRelPo.setFileSaveName(reqJson.getString("fileSaveName"));
                fileRelPo.setObjId(reqJson.getString("accidentId"));
                fileRelPo.setSaveWay("table");
                fileRelPo.setRelTypeCd("70000");
                JSONObject businessJson = BeanConvertUtil.beanCovertJson(fileRelPo);
                businessJsonArray.add(businessJson);
            }

            return sj.toString();
        }
        return null;
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarAccidentConstant.ADD_PUBCAR_ACCIDENT;
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

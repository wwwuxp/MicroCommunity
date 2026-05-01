package com.java110.api.listener.pubCarReimburse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarReimburse.IPubCarReimburseBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ServiceCodePubCarReimburseConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ConstantUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.text.ParseException;
import java.util.StringJoiner;

/**
 * 保存用车报销 侦听
 * @author hh
 * @date 2021/10/11
 */
@Java110Listener("savePubCarReimburseListener")
public class SavePubCarReimburseListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IPubCarReimburseBMO pubCarReimburseBMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(reqJson, "feeType", "请选择费用类别");
        Assert.hasKeyAndValue(reqJson, "reimburseAmount", "请填写报销金额");
        String reimburseAmount = reqJson.getString("reimburseAmount");
        if (!reimburseAmount.matches(ConstantUtil.REGEX_DECIMAL)){
            throw new IllegalArgumentException("报销金额格式不正确，请正确输入");
        }
        Assert.hasKeyAndValue(reqJson, "tripId", "请选择对应的行程");
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        reqJson.put("reimburseId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_REIMBURSE_ID));
        String invoice = "invoice";
        if (reqJson.containsKey(invoice) && CollectionUtils.isNotEmpty(reqJson.getJSONArray(invoice))) {
            JSONArray businessJsonArray = new JSONArray();
            StringJoiner sj = new StringJoiner(",");
            JSONArray invoiceArray = reqJson.getJSONArray(invoice);

            for (int i = 0, size = invoiceArray.size(); i < size; i++) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(invoiceArray.getString(i));
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

                sj.add(fileDto.getFileId());
                reqJson.put("fileSaveName", fileName);

                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId("-1");
                fileRelPo.setFileRealName(fileDto.getFileId());
                fileRelPo.setFileSaveName(reqJson.getString("fileSaveName"));
                fileRelPo.setObjId(reqJson.getString("reimburseId"));
                fileRelPo.setSaveWay("table");
                fileRelPo.setRelTypeCd("70000");
                JSONObject businessJson = BeanConvertUtil.beanCovertJson(fileRelPo);
                businessJsonArray.add(businessJson);
            }
            reqJson.put("invoice", sj.toString());
            handleBusinessJsonArrayData(context, businessJsonArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL, FileRelPo.class);
        }

        pubCarReimburseBMOImpl.addPubCarReimburse(reqJson, context);
    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarReimburseConstant.ADD_PUBCAR_REIMBURSE;
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

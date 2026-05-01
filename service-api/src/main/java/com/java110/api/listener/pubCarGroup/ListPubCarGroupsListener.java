package com.java110.api.listener.pubCarGroup;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarGroupInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarGroupConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.utils.util.ConstantUtil;
import com.java110.vo.api.pubCarGroup.ApiPubCarGroupDataVo;
import com.java110.vo.api.pubCarGroup.ApiPubCarGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

/**
 * 查询分组侦听类
 */
@Java110Listener("listPubCarGroupsListener")
public class ListPubCarGroupsListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarGroupInnerServiceSMO pubCarGroupInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarGroupConstant.LIST_PUBCARGROUPS;
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
        Assert.hasKeyAndValue(reqJson, "communityId", "请求中未包含communityId");
        if (reqJson.containsKey(ConstantUtil.SIMPLE_FLAG) && ConstantUtil.SIMPLE_FLAG_YES.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))) {
            return;
        }
        super.validatePageInfo(reqJson);
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        // 只获取分组id和分组名称
        if (reqJson.containsKey(ConstantUtil.SIMPLE_FLAG) && ConstantUtil.SIMPLE_FLAG_YES.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))) {
            getGroupNames(context, reqJson);
            return;
        }


        PubCarGroupDto pubCarGroupDto = BeanConvertUtil.covertBean(reqJson, PubCarGroupDto.class);

        int count = pubCarGroupInnerServiceSMOImpl.queryPubCarGroupsCount(pubCarGroupDto);

        List<ApiPubCarGroupDataVo> pubCarGroups;

        if (count > 0) {
            pubCarGroups = BeanConvertUtil.covertBeanList(pubCarGroupInnerServiceSMOImpl.queryPubCarGroups(pubCarGroupDto), ApiPubCarGroupDataVo.class);
        } else {
            pubCarGroups = Collections.emptyList();
        }

        ApiPubCarGroupVo apiPubCarGroupVo = new ApiPubCarGroupVo();

        apiPubCarGroupVo.setTotal(count);
        apiPubCarGroupVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarGroupVo.setData(pubCarGroups);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPubCarGroupVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 只获取分组名字
     *
     * @param context
     * @param reqJson
     */
    private void getGroupNames(DataFlowContext context, JSONObject reqJson) {
        String communityId = reqJson.getString("communityId");
        List<ApiPubCarGroupDataVo> pubCarGroupNames = pubCarGroupInnerServiceSMOImpl.queryPubCarGroupNames(communityId);

        ApiPubCarGroupVo apiPubCarGroupVo = new ApiPubCarGroupVo();
        apiPubCarGroupVo.setData(pubCarGroupNames);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarGroupVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}

package com.java110.front.components.govWechatConfig;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.dto.mapping.MappingDto;
import com.java110.intf.community.IMappingInnerServiceSMO;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author hh
 * @date 2022/3/28
 */
@Component("govWechatConfig")
public class GetGovWechatConfigComponent {

    @Autowired
    private IMappingInnerServiceSMO mappingInnerServiceSMOImpl;

    /**
     * 更新政务微信配置状态
     * @param pd 前端请求参数封装
     * @return ResponseEntity
     */
    public ResponseEntity<String> update(IPageData pd){
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "id", "id不能为空");
        Assert.hasKeyAndValue(paramIn, "value", "value不能为空");

        MappingDto mappingDto = new MappingDto();
        mappingDto.setId(paramIn.getString("id"));
        mappingDto.setValue(paramIn.getString("value"));
        int updateMapping = mappingInnerServiceSMOImpl.updateMapping(mappingDto);
        if (updateMapping > 0){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }
        return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 获取政务微信配置这条数据
     * @return ResponseEntity
     */
    public ResponseEntity<String> get(IPageData pd){
        MappingDto mappingDto = new MappingDto();
        mappingDto.setKey("GOV_WECHAT_ENABLE");
        List<MappingDto> mappingDtoList = mappingInnerServiceSMOImpl.queryMappings(mappingDto);

        if (mappingDtoList != null && mappingDtoList.size() == 1){
            JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
            Integer isApp = paramIn.getInteger("isApp");
            if (Objects.equals(isApp, 1)){
                MappingDto mappingDtoResult = mappingDtoList.get(0);
                JSONObject resultJson = new JSONObject();
                resultJson.put("id", Integer.parseInt(mappingDtoResult.getId()));
                resultJson.put("value", "true".equals(mappingDtoResult.getValue()));
                return new ResponseEntity<>(resultJson.toJSONString(), HttpStatus.OK);
            }

            MappingDto mappingDtoResult = mappingDtoList.get(0);
            JSONObject resultJson = new JSONObject();
            resultJson.put("id", Integer.parseInt(mappingDtoResult.getId()));
            resultJson.put("value", "true".equals(mappingDtoResult.getValue()));
            resultJson.put("name", mappingDtoResult.getName());
            resultJson.put("remark", mappingDtoResult.getRemark());
            return new ResponseEntity<>(resultJson.toJSONString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new JSONObject().toJSONString(), HttpStatus.OK);
    }

}

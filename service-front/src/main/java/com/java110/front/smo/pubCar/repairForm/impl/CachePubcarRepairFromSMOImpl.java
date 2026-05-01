package com.java110.front.smo.pubCar.repairForm.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.dto.pubCar.repaorForm.PubcarRepairFormDto;
import com.java110.front.smo.pubCar.repairForm.CachePubcarRepairFromSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.ServiceCodePubcarRepairFromConstant;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author lmf
 * @date 2022/7/26
 */
@Service("cachePubcarRepairFromSMOImpl")
public class CachePubcarRepairFromSMOImpl implements CachePubcarRepairFromSMO {

    /**
     * 添加保存
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> save(IPageData pd){
        String key = ServiceCodePubcarRepairFromConstant.PREFIX + pd.getUserId();
        if(StringUtils.isEmpty(pd.getReqData())){
            throw new SMOException("数据为空");
        }
        PubcarRepairFormDto repairForm = JSONObject.parseObject(pd.getReqData(), PubcarRepairFormDto.class);
        // 数据保存2周
        CommonCache.setValue(key, JSONObject.toJSONString(repairForm), 60 * 60 * 24 * 14);
        return ResponseEntity.ok("成功");
    }

    /**
     * 获取保存
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> get(IPageData pd) {
        String key = ServiceCodePubcarRepairFromConstant.PREFIX + pd.getUserId();
        String value = CommonCache.getValue(key);
        if(StringUtils.isEmpty(value)){
            value = JSONObject.toJSONString(new PubcarRepairFormDto());
        }
        return ResponseEntity.ok(value);
    }

}

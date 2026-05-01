package com.hezong.pubcar.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.BusinessServiceDataFlow;

/**
 * @author hh
 * @datetime 2021/7/15 14:18
 */
public interface IPubCarServiceSMO {

    JSONObject service(BusinessServiceDataFlow businessServiceDataFlow);

}

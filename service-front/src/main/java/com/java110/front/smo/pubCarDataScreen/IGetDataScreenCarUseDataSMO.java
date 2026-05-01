package com.java110.front.smo.pubCarDataScreen;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @datetime 2021/8/17 16:57
 */
public interface IGetDataScreenCarUseDataSMO {

    ResponseEntity<String> getCarUseData(IPageData pd);

}

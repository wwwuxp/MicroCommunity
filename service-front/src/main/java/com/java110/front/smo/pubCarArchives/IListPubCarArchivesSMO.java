package com.java110.front.smo.pubCarArchives;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author hh
 * @datetime 2021/8/6 11:23
 */
public interface IListPubCarArchivesSMO {

    /**
     * 查询车辆档案列表
     * @param pd
     * @return
     */
    ResponseEntity<String> listPubCarArchives(IPageData pd);

}

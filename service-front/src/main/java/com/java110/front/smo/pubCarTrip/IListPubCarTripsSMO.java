package com.java110.front.smo.pubCarTrip;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 行程管理表管理服务接口类
 * add by hh
 */
public interface IListPubCarTripsSMO {

    /**
     * web后端行程列表信息
     * @param pd 请求数据封装
     * @return responseEntity
     * @throws SMOException
     */
    ResponseEntity<String> listPubCarTrips(IPageData pd) throws SMOException;

    /**
     * 微信小程序端查询司机的所有行程信息
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> listDriverTrips(IPageData pd);

    /**
     * 司机用车行程统计
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> listDriverTripStatistics(IPageData pd);

    /**
     * 部门用车行程统计
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> listDepartmentTripStatistics(IPageData pd);

    /**
     * 车辆行程统计
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> listCarTripStatistics(IPageData pd);

    /**
     * 选择行程
     * @param pd 请求数据封装
     * @return responseEntity
     */
    ResponseEntity<String> choosePubCarTrip(IPageData pd);

}

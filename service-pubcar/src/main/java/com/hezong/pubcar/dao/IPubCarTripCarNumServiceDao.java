package com.hezong.pubcar.dao;


import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 申请车辆数量管理表组件内部之间使用，没有给外围系统提供服务能力
 * 申请车辆数量管理表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarTripCarNumServiceDao {

    /**
     * 保存 申请车辆数量管理表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarTripCarNumInfo(Map info) throws DAOException;

    /**
     * 查询申请车辆数量管理表信息（instance过程）
     * 根据bId 查询申请车辆数量管理表信息
     * @param info bId 信息
     * @return 申请车辆数量管理表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarTripCarNumInfo(Map info) throws DAOException;

    /**
     * 修改申请车辆数量管理表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarTripCarNumInfo(Map info) throws DAOException;

    /**
     * 查询申请车辆数量管理表总数
     *
     * @param info 申请车辆数量管理表信息
     * @return 申请车辆数量管理表数量
     */
    int queryPubCarTripCarNumsCount(Map info);

    void saveBusinessPubCarTripCarNumInfo(JSONObject businessPubCarTripCarNum);

    List<Map> getBusinessTripCarNumInfo(Map info);

    List<PubCarTripCarNumDto> getTripTypeAndNoByTripId(String tripId);

    /**
     * 根据行程id和司机id查询车辆id
     * @param map 参数
     * @return string
     */
    String getCarIdByTripIdAndDriverId(Map<String, String> map);

}

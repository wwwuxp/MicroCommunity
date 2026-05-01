package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 乘车人信息管理表组件内部之间使用，没有给外围系统提供服务能力
 * 乘车人信息管理表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarTripCarPassengerServiceDao {

    /**
     * 保存 乘车人信息管理表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarTripCarPassengerInfo(Map info) throws DAOException;

    /**
     * 查询乘车人信息管理表信息（instance过程）
     * 根据bId 查询乘车人信息管理表信息
     * @param info bId 信息
     * @return 乘车人信息管理表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarTripCarPassengerInfo(Map info) throws DAOException;

    /**
     * 修改乘车人信息管理表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarTripCarPassengerInfo(Map info) throws DAOException;

    /**
     * 查询乘车人信息管理表总数
     *
     * @param info 乘车人信息管理表信息
     * @return 乘车人信息管理表数量
     */
    int queryPubCarTripCarPassengersCount(Map info);

    /**
     * 保存行程乘车人业务表信息
     * @param businessCarPassenger 数据
     */
    void saveBusinessTripCarPassengerInfo(JSONObject businessCarPassenger);

    /**
     * 获取行程乘车人业务数据
     * @param info 参数
     * @return list
     */
    List<Map> getBusinessTripCarPassengerInfo(Map info);

    /**
     * 保存行程乘车人实例数据
     * @param info 参数
     */
    void saveTripCarPassengerInfo(Map info);

}

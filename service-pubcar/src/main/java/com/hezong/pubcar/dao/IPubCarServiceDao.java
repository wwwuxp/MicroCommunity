package com.hezong.pubcar.dao;


import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCar.ExistPubCarDto;
import com.java110.dto.pubCar.PubCarDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCar.ApiCarStatisticsDataVo;
import com.java110.vo.api.pubCar.ApiSimplePubCarDataVo;
import com.java110.vo.api.pubCarArchives.ApiPubCarArchivesDataVo;

import java.util.List;
import java.util.Map;

/**
 * 车辆管理表组件内部之间使用，没有给外围系统提供服务能力
 * 车辆管理表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarServiceDao {

    /**
     * 保存 车辆管理表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarInfo(Map info) throws DAOException;

    /**
     * 查询车辆管理表信息（instance过程）
     * 根据bId 查询车辆管理表信息
     * @param info bId 信息
     * @return 车辆管理表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarInfo(Map info) throws DAOException;

    /**
     * 修改车辆管理表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarInfo(Map info) throws DAOException;

    /**
     * 查询车辆管理表总数
     *
     * @param info 车辆管理表信息
     * @return 车辆管理表数量
     */
    int queryPubCarsCount(Map info);

    /**
     * 保存车辆业务信息
     * @param businessPubCar 车辆信息
     */
    void saveBusinessPubCarInfo(JSONObject businessPubCar);

    /**
     * 获取车辆业务信息
     * @param info
     * @return
     */
    List<Map> getBusinessPubCarInfo(Map<String, Object> info);

    /**
     * 根据carId查询对应的attrId和insuranceId
     * @param carId
     * @return
     */
    List<Map<String, Object>> queryInfoByCarId(String carId);

    /**
     * 保存删除的数据到业务信息表
     * @param businessPubCarInfo
     */
    void saveDelBusinessPubCarInfo(JSONObject businessPubCarInfo);

    /**
     * 查询车辆的简单信息
     * @param map 参数
     * @return list
     */
    List<ApiSimplePubCarDataVo> querySimplePubCars(Map<String, Object> map);

    int queryCarStatisticsCount(PubCarDto pubCarDto);

    List<ApiCarStatisticsDataVo> queryCarStatistics(PubCarDto pubCarDto);

    int queryPubCarArchivesCount(PubCarDto pubCarDto);

    List<ApiPubCarArchivesDataVo> queryPubCarArchives(PubCarDto pubCarDto);

    /**
     * 车辆是否已经存在
     * @param existPubCarDto 参数
     * @return integer
     */
    ExistPubCarDto existPubCar(ExistPubCarDto existPubCarDto);

    /**
     * 获取时间段内在忙的车id集合
     * @param map 参数
     * @return list
     */
    List<PubCarDto> listNotEmptyCars(Map<String, Object> map);

    /**
     * 获取空车基本信息
     * @param param 参数
     * @return list
     */
    List<ApiSimplePubCarDataVo> listEmptyCarInfos(Map<String, Object> param);

    /**
     * 查询车辆预约和归还信息
     * @param emptyCarIds 空车id
     * @return list
     */
    List<ApiSimplePubCarDataVo> getCarReturnAndReserveInfo(List<String> emptyCarIds);

}

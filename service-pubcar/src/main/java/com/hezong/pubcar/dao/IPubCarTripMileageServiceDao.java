package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarTripMileage.ApiTripMileageAndFeeDataVo;

import java.util.List;
import java.util.Map;

/**
 * 行驶里程信息表组件内部之间使用，没有给外围系统提供服务能力
 * 行驶里程信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarTripMileageServiceDao {

    /**
     * 保存 行驶里程信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarTripMileageInfo(Map info) throws DAOException;

    /**
     * 查询行驶里程信息表信息（instance过程）
     * 根据bId 查询行驶里程信息表信息
     * @param info bId 信息
     * @return 行驶里程信息表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarTripMileageInfo(Map info) throws DAOException;

    /**
     * 修改行驶里程信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarTripMileageInfo(Map info) throws DAOException;

    /**
     * 查询行驶里程信息表总数
     *
     * @param info 行驶里程信息表信息
     * @return 行驶里程信息表数量
     */
    int queryPubCarTripMileagesCount(Map info);

    List<PubCarTripMileageDto> queryDriverTrip(PubCarTripMileageDto pubCarTripMileageDto);

    void saveBusinessPubCarTripMileageInfo(JSONObject businessPubCarTripMileage);

    List<Map> getBusinessPubCarTripMileageInfo(Map info);

    int queryDriverTripIsInProgress(PubCarTripMileageDto pubCarTripMileageDto);

    /**
     * 查询上一部车的结束里程
     * @param map 参数
     * @return Double
     */
    Double queryEndMileageOfPreviousCar(Map<String, String> map);

    /**
     * 获取行程里程登记信息
     * @param pubCarTripMileageDto pubCarTripMileageDto
     * @return ApiTripMileageAndFeeDataVo
     */
    ApiTripMileageAndFeeDataVo getTripMileageAndFee(PubCarTripMileageDto pubCarTripMileageDto);

    /**
     * 更新行程里程及费用信息
     * @param pubCarTripMileageDto pubCarTripMileageDto
     * @return int
     */
    int updateTripMileageAndFee(PubCarTripMileageDto pubCarTripMileageDto);

}

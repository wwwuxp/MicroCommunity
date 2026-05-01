package com.hezong.pubcar.dao;


import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarDriver.PubCarDriverDto;
import com.java110.dto.pubCarDriver.PubCarExistDriverDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarDriverDataVo;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarSimpleDriverDataVo;

import java.util.List;
import java.util.Map;

/**
 * 用车人管理表组件内部之间使用，没有给外围系统提供服务能力
 * 用车人管理表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarDriverServiceDao {

    /**
     * 保存 用车人管理表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarDriverInfo(Map info) throws DAOException;

    /**
     * 查询用车人管理表信息（instance过程）
     * 根据bId 查询用车人管理表信息
     * @param info bId 信息
     * @return 用车人管理表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarDriverInfo(Map info) throws DAOException;

    /**
     * 修改用车人管理表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarDriverInfo(Map info) throws DAOException;

    /**
     * 查询用车人管理表总数
     *
     * @param info 用车人管理表信息
     * @return 用车人管理表数量
     */
    int queryPubCarDriversCount(Map info);

    void saveBusinessPubCarDriverInfo(JSONObject businessPubCarUser);

    List<Map> getBusinessPubCarDriverInfo(Map info);

    String queryPubCarDriversByValidateCondition(String jobNum);

    List<ApiPubCarSimpleDriverDataVo> queryPubCarDriverNames();

    Integer existPubCarDriver(PubCarExistDriverDto existDriverDto);

    /**
     * 查询司机列表
     * @param pubCarDriverDto 参数
     * @return list
     */
    List<ApiPubCarDriverDataVo> queryPubCarDrivers(PubCarDriverDto pubCarDriverDto);

}

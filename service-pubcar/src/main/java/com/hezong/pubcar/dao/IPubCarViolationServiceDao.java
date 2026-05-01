package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarViolation.ApiPubCarViolationDataVo;

import java.util.List;
import java.util.Map;

/**
 * 车辆违章信息表组件内部之间使用，没有给外围系统提供服务能力
 * 车辆违章信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarViolationServiceDao {

    /**
     * 保存 车辆违章信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarViolationInfo(Map info) throws DAOException;

    /**
     * 查询车辆违章信息表信息（instance过程）
     * 根据bId 查询车辆违章信息表信息
     * @param info bId 信息
     * @return 车辆违章信息表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarViolationInfo(Map info) throws DAOException;

    /**
     * 修改车辆违章信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarViolationInfo(Map info) throws DAOException;

    /**
     * 查询车辆违章信息表总数
     *
     * @param info 车辆违章信息表信息
     * @return 车辆违章信息表数量
     */
    int queryPubCarViolationsCount(PubCarViolationDto info);

    /**
     * 保存公车违章业务表信息
     * @param businessPubCarViolation 数据
     */
    void saveBusinessPubCarViolationInfo(JSONObject businessPubCarViolation);

    /**
     * 获取公车违章业务表信息
     * @param info info
     * @return list
     */
    List<Map> getBusinessPubCarViolationInfo(Map<String, Object> info);

    /**
     * 查询车辆违章信息
     * @param pubCarViolationDto 参数
     * @return list
     */
    List<ApiPubCarViolationDataVo> listPubCarViolations(PubCarViolationDto pubCarViolationDto);

    /**
     * 根据司机id
     * @param pubCarViolationDto 参数
     * @return list
     */
    List<ApiPubCarViolationDataVo> listPubCarDriverViolations(PubCarViolationDto pubCarViolationDto);

}

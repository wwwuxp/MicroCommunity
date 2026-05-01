package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.outGoing.ApiOutGoingDataVo;
import com.java110.vo.api.outGoing.ApiOutGoingStatisticsDataVo;
import com.java110.vo.api.outGoing.ApiUserOutGoingDataVo;

import java.util.List;
import java.util.Map;

/**
 * 外出信息表组件内部之间使用，没有给外围系统提供服务能力
 * 外出信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IOutGoingServiceDao {

    /**
     * 保存 外出信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOutGoingInfo(Map info) throws DAOException;

    /**
     * 查询外出信息表信息（instance过程）
     * 根据bId 查询外出信息表信息
     * @param info bId 信息
     * @return 外出信息表信息
     * @throws DAOException DAO异常
     */
    List<ApiOutGoingDataVo> getOutGoingInfo(OutGoingDto info) throws DAOException;

    /**
     * 修改外出信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOutGoingInfo(Map info) throws DAOException;

    /**
     * 查询外出信息表总数
     *
     * @param info 外出信息表信息
     * @return 外出信息表数量
     */
    int queryOutGoingsCount(Map info);


    void saveBusinessOutGoingInfo(JSONObject businessOutGoing);

    List<Map> getBusinessOutGoingInfo(Map info);

    /**
     * 查询移动端外出记录
     * @param outGoingDto 参数
     * @return list
     */
    List<ApiUserOutGoingDataVo> queryUserOutGoings(OutGoingDto outGoingDto);

    /**
     * 查询外出统计列表
     * @param outGoingDto 请求参数
     * @return List
     */
    List<ApiOutGoingStatisticsDataVo> queryOutGoingStatistics(OutGoingDto outGoingDto);

    /**
     * 查询外出统计数量
     * @param outGoingDto 请求参数
     * @return int
     */
    int queryOutGoingStatisticsCount(OutGoingDto outGoingDto);

}

package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 外出人员信息表组件内部之间使用，没有给外围系统提供服务能力
 * 外出人员信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IOutGoingPersonnelServiceDao {

    /**
     * 保存 外出人员信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOutGoingPersonnelInfo(Map info) throws DAOException;

    /**
     * 查询外出人员信息表信息（instance过程）
     * 根据bId 查询外出人员信息表信息
     * @param info bId 信息
     * @return 外出人员信息表信息
     * @throws DAOException DAO异常
     */
    List<Map> getOutGoingPersonnelInfo(Map info) throws DAOException;

    /**
     * 修改外出人员信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOutGoingPersonnelInfo(Map info) throws DAOException;

    /**
     * 查询外出人员信息表总数
     *
     * @param info 外出人员信息表信息
     * @return 外出人员信息表数量
     */
    int queryOutGoingPersonnelsCount(Map info);

    void saveBusinessOutGoingPersonnelInfo(JSONObject businessOutGoingPersonnel);

    List<Map> getBusinessOutGoingPersonnelInfo(Map info);

    List<OutGoingDto> listOutGoingTimesByUserInfo(UserInfoDto userInfoDto);

    /**
     * 该时间端是否有外出
     * @param userInfoDto 参数封装
     * @return integer
     */
    Integer isOutGoing(UserInfoDto userInfoDto);
}

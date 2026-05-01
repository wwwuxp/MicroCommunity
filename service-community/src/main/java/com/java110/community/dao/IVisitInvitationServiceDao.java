package com.java110.community.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.visitInvitation.VisitInvitationDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;

import java.util.List;
import java.util.Map;

/**
 * 访客邀约信息表组件内部之间使用，没有给外围系统提供服务能力
 * 访客邀约信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IVisitInvitationServiceDao {

    /**
     * 保存 访客邀约信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveVisitInvitationInfo(Map info) throws DAOException;

    /**
     * 保存访客邀约业务表信息
     * @param visitInvitation 数据
     */
    void saveBusinessVisitInvitationInfo(JSONObject visitInvitation);

    /**
     * 获取访客邀约业务表信息
     * @param info 参数
     * @return lsit
     */
    List<Map<String, Object>> getBusinessVisitInvitationInfo(Map<String, String> info);

    /**
     * 是否有相同的唯一码
     * @param map map
     * @return integer
     */
    Integer hasSameUniqueCode(Map<String, String> map);

    /**
     * 修改访客邀约信息
     * @param businessVisitInvitationInfo 数据
     */
    void updateVisitInvitationInfo(Map<String, Object> businessVisitInvitationInfo);

    /**
     * 获取访客信息
     * @param info 参数
     * @return list
     */
    List<Map<String, Object>> getVisitInvitationInfo(Map<String, String> info);

    /**
     * 查询访客邀约数量
     * @param visitInvitationDto 参数
     * @return int
     */
    int queryVisitInvitationsCount(VisitInvitationDto visitInvitationDto);

    /**
     * 查询访客邀约列表
     * @param visitInvitationDto 参数
     * @return list
     */
    List<ApiVisitInvitationDataVo> listVisitInvitations(VisitInvitationDto visitInvitationDto);

}

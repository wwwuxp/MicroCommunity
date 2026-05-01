package com.java110.community.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.visitor.VisitorDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;

import java.util.List;
import java.util.Map;

/**
 * 访客邀约人信息表组件内部之间使用，没有给外围系统提供服务能力
 * 访客邀约人信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IVisitorServiceDao {

    /**
     * 保存 访客邀约人信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void saveVisitorInfo(Map info) throws DAOException;

    /**
     * 查询访客邀约人信息表信息（instance过程）
     * 根据bId 查询访客邀约人信息表信息
     * @return 访客邀约人信息表信息
     * @throws DAOException DAO异常
     */
    List<Map<String, Object>> getVisitorInfo(Map<String, Object> info) throws DAOException;

    /**
     * 修改访客邀约人信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateVisitorInfo(Map info) throws DAOException;

    /**
     * 查询访客邀约人信息表总数
     *
     * @param info 访客邀约人信息表信息
     * @return 访客邀约人信息表数量
     */
    int queryVisitorsCount(Map info);

    void saveBusinessVisitorInfo(JSONObject visitor);

    List<Map<String, Object>> getBusinessVisitorInfo(Map<String, String> info);

    List<ApiVisitInvitationDataVo> listVisitors(VisitorDto visitorDto);

}

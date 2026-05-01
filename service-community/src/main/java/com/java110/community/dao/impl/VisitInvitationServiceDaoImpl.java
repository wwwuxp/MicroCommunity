package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitInvitationServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.visitInvitation.VisitInvitationDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 访客邀约信息表服务 与数据库交互
 * Created by hh
 */
@Service("visitInvitationServiceDaoImpl")
//@Transactional
public class VisitInvitationServiceDaoImpl extends BaseServiceDao implements IVisitInvitationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(VisitInvitationServiceDaoImpl.class);

    /**
     * 保存访客邀约信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveVisitInvitationInfo(Map info) throws DAOException {
        logger.debug("保存访客邀约信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("visitInvitationServiceDaoImpl.saveVisitInvitationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存访客邀约信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void saveBusinessVisitInvitationInfo(JSONObject info) {
        int saveFlag = sqlSessionTemplate.insert("visitInvitationServiceDaoImpl.saveBusinessVisitInvitationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存访客邀约信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map<String, Object>> getBusinessVisitInvitationInfo(Map<String, String> info) {
        return sqlSessionTemplate.selectList("visitInvitationServiceDaoImpl.getBusinessVisitInvitationInfo", info);
    }

    @Override
    public Integer hasSameUniqueCode(Map<String, String> info) {
        return sqlSessionTemplate.selectOne("visitInvitationServiceDaoImpl.hasSameUniqueCode", info);
    }

    @Override
    public void updateVisitInvitationInfo(Map<String, Object> info) {
        int saveFlag = sqlSessionTemplate.update("visitInvitationServiceDaoImpl.updateVisitInvitationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改访客邀约信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map<String, Object>> getVisitInvitationInfo(Map<String, String> info) {
        return sqlSessionTemplate.selectList("visitInvitationServiceDaoImpl.getVisitInvitationInfo", info);
    }

    @Override
    public int queryVisitInvitationsCount(VisitInvitationDto visitInvitationDto) {
        return sqlSessionTemplate.selectOne("visitInvitationServiceDaoImpl.queryVisitInvitationsCount", visitInvitationDto);
    }

    @Override
    public List<ApiVisitInvitationDataVo> listVisitInvitations(VisitInvitationDto visitInvitationDto) {
        return sqlSessionTemplate.selectList("visitInvitationServiceDaoImpl.listVisitInvitations", visitInvitationDto);
    }
}

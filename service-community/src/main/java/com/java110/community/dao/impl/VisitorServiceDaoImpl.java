package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IVisitorServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.visitor.VisitorDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 访客邀约人信息表服务 与数据库交互
 * Created by hh
 */
@Service("visitorServiceDaoImpl")
//@Transactional
public class VisitorServiceDaoImpl extends BaseServiceDao implements IVisitorServiceDao {

    private static Logger logger = LoggerFactory.getLogger(VisitorServiceDaoImpl.class);

    /**
     * 保存访客邀约人信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveVisitorInfo(Map info) throws DAOException {
        logger.debug("保存访客邀约人信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("visitorServiceDaoImpl.saveVisitorInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存访客邀约人信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询访客邀约人信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map<String, Object>> getVisitorInfo(Map<String, Object> info) throws DAOException {
        return sqlSessionTemplate.selectList("visitorServiceDaoImpl.getVisitorInfo", info);
    }


    /**
     * 修改访客邀约人信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateVisitorInfo(Map info) throws DAOException {
        logger.debug("修改访客邀约人信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("visitorServiceDaoImpl.updateVisitorInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改访客邀约人信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询访客邀约人信息表数量
     *
     * @param info 访客邀约人信息表信息
     * @return 访客邀约人信息表数量
     */
    @Override
    public int queryVisitorsCount(Map info) {
        logger.debug("查询访客邀约人信息表数据 入参 info : {}", info);

        List<Map> businessVisitorInfos = sqlSessionTemplate.selectList("visitorServiceDaoImpl.queryVisitorsCount", info);
        if (businessVisitorInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessVisitorInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessVisitorInfo(JSONObject info) {
        int saveFlag = sqlSessionTemplate.insert("visitorServiceDaoImpl.saveBusinessVisitorInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存访客邀约人信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map<String, Object>> getBusinessVisitorInfo(Map<String, String> info) {
        return sqlSessionTemplate.selectList("visitorServiceDaoImpl.getBusinessVisitorInfo", info);
    }

    @Override
    public List<ApiVisitInvitationDataVo> listVisitors(VisitorDto info) {
        return sqlSessionTemplate.selectList("visitorServiceDaoImpl.listVisitors", info);
    }
}

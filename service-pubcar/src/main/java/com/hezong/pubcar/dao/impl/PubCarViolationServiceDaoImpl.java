package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarViolationServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarViolation.ApiPubCarViolationDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 车辆违章信息表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarViolationServiceDaoImpl")
//@Transactional
public class PubCarViolationServiceDaoImpl extends BaseServiceDao implements IPubCarViolationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarViolationServiceDaoImpl.class);

    /**
     * 保存车辆违章信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarViolationInfo(Map info) throws DAOException {
        logger.debug("保存车辆违章信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarViolationServiceDaoImpl.savePubCarViolationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆违章信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询车辆违章信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarViolationInfo(Map info) throws DAOException {
        logger.debug("查询车辆违章信息表信息 入参 info : {}", info);

        return sqlSessionTemplate.selectList("pubCarViolationServiceDaoImpl.getPubCarViolationInfo", info);
    }

    /**
     * 修改车辆违章信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarViolationInfo(Map info) throws DAOException {
        logger.debug("修改车辆违章信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarViolationServiceDaoImpl.updatePubCarViolationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改车辆违章信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询车辆违章信息表数量
     *
     * @param info 车辆违章信息表信息
     * @return 车辆违章信息表数量
     */
    @Override
    public int queryPubCarViolationsCount(PubCarViolationDto info) {
        logger.debug("查询车辆违章信息表数据 入参 info : {}", info);

        return sqlSessionTemplate.selectOne("pubCarViolationServiceDaoImpl.queryPubCarViolationsCount", info);
    }

    @Override
    public void saveBusinessPubCarViolationInfo(JSONObject info) {
        logger.debug("保存车辆违章信息表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarViolationServiceDaoImpl.saveBusinessPubCarViolationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆违章信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarViolationInfo(Map<String, Object> info) {
        logger.debug("查询车辆违章信息表信息 入参 info : {}", info);

        return sqlSessionTemplate.selectList("pubCarViolationServiceDaoImpl.getBusinessPubCarViolationInfo", info);
    }

    @Override
    public List<ApiPubCarViolationDataVo> listPubCarViolations(PubCarViolationDto info) {
        return sqlSessionTemplate.selectList("pubCarViolationServiceDaoImpl.listPubCarViolations", info);
    }

    @Override
    public List<ApiPubCarViolationDataVo> listPubCarDriverViolations(PubCarViolationDto info) {
        return sqlSessionTemplate.selectList("pubCarViolationServiceDaoImpl.listPubCarDriverViolations", info);
    }
}

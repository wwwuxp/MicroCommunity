package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripCarPassengerServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 乘车人信息管理表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarTripCarPassengerServiceDaoImpl")
public class PubCarTripCarPassengerServiceDaoImpl extends BaseServiceDao implements IPubCarTripCarPassengerServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarTripCarPassengerServiceDaoImpl.class);

    /**
     * 保存乘车人信息管理表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarTripCarPassengerInfo(Map info) throws DAOException {
        logger.debug("保存乘车人信息管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripCarPassengerServiceDaoImpl.savePubCarTripCarPassengerInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存乘车人信息管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询乘车人信息管理表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarTripCarPassengerInfo(Map info) throws DAOException {
        logger.debug("查询乘车人信息管理表信息 入参 info : {}", info);

        List<Map> businessPubCarTripCarPassengerInfos = sqlSessionTemplate.selectList("pubCarTripCarPassengerServiceDaoImpl.getPubCarTripCarPassengerInfo", info);

        return businessPubCarTripCarPassengerInfos;
    }


    /**
     * 修改乘车人信息管理表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarTripCarPassengerInfo(Map info) throws DAOException {
        logger.debug("修改乘车人信息管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarTripCarPassengerServiceDaoImpl.updatePubCarTripCarPassengerInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改乘车人信息管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询乘车人信息管理表数量
     *
     * @param info 乘车人信息管理表信息
     * @return 乘车人信息管理表数量
     */
    @Override
    public int queryPubCarTripCarPassengersCount(Map info) {
        logger.debug("查询乘车人信息管理表数据 入参 info : {}", info);

        List<Map> businessPubCarTripCarPassengerInfos = sqlSessionTemplate.selectList("pubCarTripCarPassengerServiceDaoImpl.queryPubCarTripCarPassengersCount", info);
        if (businessPubCarTripCarPassengerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarTripCarPassengerInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessTripCarPassengerInfo(JSONObject info) {
        logger.debug("保存乘车人信息管理表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripCarPassengerServiceDaoImpl.saveBusinessTripCarPassengerInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存乘车人信息管理表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessTripCarPassengerInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarTripCarPassengerServiceDaoImpl.getBusinessTripCarPassengerInfo", info);
    }

    @Override
    public void saveTripCarPassengerInfo(Map info) {
        logger.debug("保存乘车人信息管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripCarPassengerServiceDaoImpl.saveTripCarPassengerInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存乘车人信息管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }
}

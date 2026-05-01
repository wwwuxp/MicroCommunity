package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripCarNumServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarTripCarNum.PubCarTripCarNumDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 申请车辆数量管理表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarTripCarNumServiceDaoImpl")
public class PubCarTripCarNumServiceDaoImpl extends BaseServiceDao implements IPubCarTripCarNumServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarTripCarNumServiceDaoImpl.class);

    /**
     * 保存申请车辆数量管理表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarTripCarNumInfo(Map info) throws DAOException {
        logger.debug("保存申请车辆数量管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripCarNumServiceDaoImpl.savePubCarTripCarNumInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存申请车辆数量管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询申请车辆数量管理表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarTripCarNumInfo(Map info) throws DAOException {
        logger.debug("查询申请车辆数量管理表信息 入参 info : {}", info);

        List<Map> businessPubCarTripCarNumInfos = sqlSessionTemplate.selectList("pubCarTripCarNumServiceDaoImpl.getPubCarTripCarNumInfo", info);

        return businessPubCarTripCarNumInfos;
    }


    /**
     * 修改申请车辆数量管理表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarTripCarNumInfo(Map info) throws DAOException {
        logger.debug("修改申请车辆数量管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarTripCarNumServiceDaoImpl.updatePubCarTripCarNumInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改申请车辆数量管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询申请车辆数量管理表数量
     *
     * @param info 申请车辆数量管理表信息
     * @return 申请车辆数量管理表数量
     */
    @Override
    public int queryPubCarTripCarNumsCount(Map info) {
        logger.debug("查询申请车辆数量管理表数据 入参 info : {}", info);

        List<Map> businessPubCarTripCarNumInfos = sqlSessionTemplate.selectList("pubCarTripCarNumServiceDaoImpl.queryPubCarTripCarNumsCount", info);
        if (businessPubCarTripCarNumInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarTripCarNumInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessPubCarTripCarNumInfo(JSONObject info) {
        logger.debug("保存申请车辆数量管理表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripCarNumServiceDaoImpl.saveBusinessPubCarTripCarNumInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存申请车辆数量管理表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessTripCarNumInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarTripCarNumServiceDaoImpl.getBusinessPubCarTripCarNumInfo", info);
    }

    @Override
    public List<PubCarTripCarNumDto> getTripTypeAndNoByTripId(String tripId) {
        return sqlSessionTemplate.selectList("pubCarTripCarNumServiceDaoImpl.getTripTypeAndNoByTripId", tripId);
    }

    @Override
    public String getCarIdByTripIdAndDriverId(Map<String, String> map) {
        return sqlSessionTemplate.selectOne("pubCarTripCarNumServiceDaoImpl.getCarIdByTripIdAndDriverId", map);
    }
}

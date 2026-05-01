package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarRepairServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 车辆维修信息表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarRepairServiceDaoImpl")
//@Transactional
public class PubCarRepairServiceDaoImpl extends BaseServiceDao implements IPubCarRepairServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarRepairServiceDaoImpl.class);

    /**
     * 保存车辆维修信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarRepairInfo(Map info) throws DAOException {
        logger.debug("保存车辆维修信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarRepairServiceDaoImpl.savePubCarRepairInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆维修信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询车辆维修信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarRepairInfo(Map info) throws DAOException {
        logger.debug("查询车辆维修信息表信息 入参 info : {}", info);

        List<Map> businessPubCarRepairInfos = sqlSessionTemplate.selectList("pubCarRepairServiceDaoImpl.getPubCarRepairInfo", info);

        return businessPubCarRepairInfos;
    }


    /**
     * 修改车辆维修信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarRepairInfo(Map info) throws DAOException {
        logger.debug("修改车辆维修信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarRepairServiceDaoImpl.updatePubCarRepairInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改车辆维修信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询车辆维修信息表数量
     *
     * @param info 车辆维修信息表信息
     * @return 车辆维修信息表数量
     */
    @Override
    public int queryPubCarRepairsCount(Map info) {
        logger.debug("查询车辆维修信息表数据 入参 info : {}", info);

        List<Map> businessPubCarRepairInfos = sqlSessionTemplate.selectList("pubCarRepairServiceDaoImpl.queryPubCarRepairsCount", info);
        if (businessPubCarRepairInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarRepairInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessPubCarRepairInfo(JSONObject info) {
        logger.debug("保存车辆维修信息表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarRepairServiceDaoImpl.saveBusinessPubCarRepairInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆维修信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarRepairInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarRepairServiceDaoImpl.getBusinessPubCarRepairInfo", info);
    }
}

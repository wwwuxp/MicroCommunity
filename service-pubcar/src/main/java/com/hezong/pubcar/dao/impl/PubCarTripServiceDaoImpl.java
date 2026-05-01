package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.dto.pubCarTrip.PubCarTripSimpleDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.hezong.pubcar.dao.IPubCarTripServiceDao;
import com.java110.vo.api.pubCarDataScreen.ApiPubCarDataScreenOverviewAndAnalysisDataVo;
import com.java110.vo.api.pubCarDataScreen.CarTripDataVo;
import com.java110.vo.api.pubCarTrip.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 行程管理表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarTripServiceDaoImpl")
public class PubCarTripServiceDaoImpl extends BaseServiceDao implements IPubCarTripServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarTripServiceDaoImpl.class);

    /**
     * 保存行程管理表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarTripInfo(Map info) throws DAOException {
        logger.debug("保存行程管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripServiceDaoImpl.savePubCarTripInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存行程管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询行程管理表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<ApiPubCarTripDataVo> getPubCarTripInfo(PubCarTripDto info) throws DAOException {
        logger.debug("查询行程管理表信息 入参 info : {}", info);

        List<ApiPubCarTripDataVo> businessPubCarTripInfos = sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.getPubCarTripInfo", info);

        return businessPubCarTripInfos;
    }


    /**
     * 修改行程管理表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarTripInfo(Map info) throws DAOException {
        logger.debug("修改行程管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarTripServiceDaoImpl.updatePubCarTripInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改行程管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询行程管理表数量
     *
     * @param info 行程管理表信息
     * @return 行程管理表数量
     */
    @Override
    public int queryPubCarTripsCount(PubCarTripDto info) {
        logger.debug("查询行程管理表数据 入参 info : {}", info);

        List<Map> businessPubCarTripInfos = sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarTripsCount", info);
        if (businessPubCarTripInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarTripInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessPubCarTripInfo(JSONObject info) {
        logger.debug("保存行程管理表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripServiceDaoImpl.saveBusinessPubCarTripInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存行程管理表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarTripInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.getBusinessPubCarTripInfo", info);
    }

    @Override
    public int queryPubCarDriverTripsCount(PubCarTripSimpleDto info) {
        List<Map> businessPubCarTripInfos = sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarDriverTripsCount", info);
        if (businessPubCarTripInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarTripInfos.get(0).get("count").toString());
    }

    @Override
    public List<ApiPubCarTripDataVo> queryPubCarDriverTrips(PubCarTripSimpleDto info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarDriverTrips", info);
    }

    @Override
    public int queryPubCarSimpleTripsCount(PubCarTripSimpleDto info) {
        List<Map> businessPubCarTripInfos = sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarSimpleTripsCount", info);
        if (businessPubCarTripInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarTripInfos.get(0).get("count").toString());
    }

    @Override
    public List<ApiSimpleTripDataVo> queryPubCarSimpleTrips(PubCarTripSimpleDto info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarSimpleTrips", info);
    }

    @Override
    public int queryCarTripStatisticsCount(PubCarTripSimpleDto info) {
        List<Map> businessPubCarTripInfos = sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryCarTripStatisticsCount", info);
        if (businessPubCarTripInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessPubCarTripInfos.get(0).get("count").toString());
    }

    @Override
    public List<ApiCarTripStatisticsDataVo> queryCarTripStatistics(PubCarTripSimpleDto info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryCarTripStatistics", info);
    }

    @Override
    public ApiPubCarDataScreenOverviewAndAnalysisDataVo queryPubCarDataScreenOverview(Map<String, Object> info) {
        return sqlSessionTemplate.selectOne("pubCarTripServiceDaoImpl.queryPubCarDataScreenOverview", info);
    }

    @Override
    public Map<String, Object> queryPubCarDataScreenSingleDayOverview(Map<String, Object> info) {
        return sqlSessionTemplate.selectOne("pubCarTripServiceDaoImpl.queryPubCarDataScreenSingleDayOverview", info);
    }

    @Override
    public List<CarTripDataVo> queryPubCarDataScreenCarUseData(Map<String, Object> info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarDataScreenCarUseData", info);
    }

    @Override
    public List<Map<String, Object>> queryPubCarDataScreenTodayCarUseStatistics(Map<String, Object> info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarDataScreenTodayCarUseStatistics", info);
    }

    @Override
    public int queryPubCarDataScreenCarUseDataCount(Map<String, Object> info) {
        List<Map> todayCarUseDataCount = sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarDataScreenCarUseDataCount", info);
        if (todayCarUseDataCount.size() < 1) {
            return 0;
        }
        return Integer.parseInt(todayCarUseDataCount.get(0).get("count").toString());
    }

    @Override
    public List<Map<String, Object>> queryPubCarDataScreenTomorrowOrOneWeekCarUseStatistics(Map<String, Object> info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarDataScreenTomorrowOrOneWeekCarUseStatistics", info);
    }

    @Override
    public List<Map> getPubCarTripInstanceInfo(Map<String, String> info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.getPubCarTripInstanceInfo", info);
    }

    @Override
    public int listChooseTripsCount(PubCarTripSimpleDto info) {
        return sqlSessionTemplate.selectOne("pubCarTripServiceDaoImpl.listChooseTripsCount", info);
    }

    @Override
    public List<ApiChooseTripDataVo> listChooseTrips(PubCarTripSimpleDto info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.listChooseTrips", info);
    }

    @Override
    public List<Map<String, String>> getDriverIdByCarIdAndViolationTimeAndCommunityId(Map<String, Object> info) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.getDriverIdByCarIdAndViolationTimeAndCommunityId", info);
    }

    @Override
    public List<PubCarTripDto> queryPubCarTripsByTripIdsBatch(List<String> tripIds) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarTripsByTripIdsBatch", tripIds);
    }

    @Override
    public int queryPubCarUseRecordsCount(PubCarTripDto pubCarTripDto) {
        return sqlSessionTemplate.selectOne("pubCarTripServiceDaoImpl.queryPubCarUseRecordsCount", pubCarTripDto);
    }

    @Override
    public List<ApiCarUseRecordDataVo> queryPubCarUseRecords(PubCarTripDto pubCarTripDto) {
        return sqlSessionTemplate.selectList("pubCarTripServiceDaoImpl.queryPubCarUseRecords", pubCarTripDto);
    }
}

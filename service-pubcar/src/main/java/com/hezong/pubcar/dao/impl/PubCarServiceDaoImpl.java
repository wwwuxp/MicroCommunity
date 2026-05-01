package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCar.ExistPubCarDto;
import com.java110.dto.pubCar.PubCarDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCar.ApiCarStatisticsDataVo;
import com.java110.vo.api.pubCar.ApiSimplePubCarDataVo;
import com.java110.vo.api.pubCarArchives.ApiPubCarArchivesDataVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 车辆管理表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarServiceDaoImpl")
public class PubCarServiceDaoImpl extends BaseServiceDao implements IPubCarServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarServiceDaoImpl.class);

    /**
     * 保存车辆管理表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarInfo(Map info) throws DAOException {
        logger.debug("保存车辆管理表信息Instance 入参 info : {}", info);
        // 保存数据到pubcar_car
        int saveCarFlag = sqlSessionTemplate.insert("pubCarServiceDaoImpl.savePubCarInfo", info);
        // 保存数据到pubcar_car_attr
        int saveAttrFlag = sqlSessionTemplate.insert("pubCarAttrServiceDaoImpl.savePubCarAttrInfo", info);
        // 保存数据到pubcar_car_insurance
        int saveInsuranceFlag = sqlSessionTemplate.insert("pubCarInsuranceServiceDaoImpl.savePubCarInsuranceInfo", info);

        if (saveCarFlag < 1 || saveAttrFlag < 1 || saveInsuranceFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询车辆管理表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarInfo(Map info) throws DAOException {
        logger.debug("查询车辆管理表信息 入参 info : {}", info);

        List<Map> businessPubCarInfos = sqlSessionTemplate.selectList("pubCarServiceDaoImpl.getPubCarInfo", info);

        return businessPubCarInfos;
    }


    /**
     * 修改车辆管理表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarInfo(Map info) throws DAOException {
        logger.debug("修改车辆管理表信息Instance 入参 info : {}", info);

        try {
            // 更新对应的表信息
            sqlSessionTemplate.update("pubCarServiceDaoImpl.updatePubCarInfo", info);

            sqlSessionTemplate.update("pubCarAttrServiceDaoImpl.updatePubCarAttrInfo", info);

            sqlSessionTemplate.update("pubCarInsuranceServiceDaoImpl.updatePubCarInsuranceInfo", info);
        } catch (Exception e) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改车辆管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }

    }

    /**
     * 查询车辆管理表数量
     *
     * @param info 车辆管理表信息
     * @return 车辆管理表数量
     */
    @Override
    public int queryPubCarsCount(Map info) {
        logger.debug("查询车辆管理表数据 入参 info : {}", info);

        List<Map> businessPubCarInfos = sqlSessionTemplate.selectList("pubCarServiceDaoImpl.queryPubCarsCount", info);
        if (businessPubCarInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarInfos.get(0).get("count").toString());
    }


    @Override
    public void saveBusinessPubCarInfo(JSONObject businessPubCarInfo) {
        logger.debug("保存车辆管理表信息Business 入参 info : {}", businessPubCarInfo);
        int saveAttrFlag = 0;
        int saveInsuranceFlag = 0;
        // 保存数据到business_pubcar_car
        int savePubCarFlag = sqlSessionTemplate.insert("pubCarServiceDaoImpl.saveBusinessPubCarInfo", businessPubCarInfo);
        if (!businessPubCarInfo.containsKey("confirmFlag")) {
            // 保存数据到business_pubcar_car_attr
            saveAttrFlag = sqlSessionTemplate.insert("pubCarAttrServiceDaoImpl.saveBusinessPubCarAttrInfo", businessPubCarInfo.get("attr"));
            // 保存数据到business_pubcar_car_insurance
            saveInsuranceFlag = sqlSessionTemplate.insert("pubCarInsuranceServiceDaoImpl.saveBusinessPubCarInsuranceInfo", businessPubCarInfo.get("insurance"));
            if (savePubCarFlag < 1 || saveAttrFlag < 1 || saveInsuranceFlag < 1) {
                throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆管理表信息Instance数据失败：" + JSONObject.toJSONString(businessPubCarInfo));
            }
        } else {
            // 保存数据到business_pubcar_car_insurance
            saveInsuranceFlag = sqlSessionTemplate.insert("pubCarInsuranceServiceDaoImpl.saveBusinessPubCarInsuranceInfo", businessPubCarInfo);
            if (savePubCarFlag < 1 || saveInsuranceFlag < 1) {
                throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆管理表信息Instance数据失败：" + JSONObject.toJSONString(businessPubCarInfo));
            }
        }

    }

    @Override
    public List<Map> getBusinessPubCarInfo(Map<String, Object> info) {
        logger.debug("查询车辆管理业务表信息 入参 info : {}", info);

        List<Map> businessPubCarInfos = sqlSessionTemplate.selectList("pubCarServiceDaoImpl.getBusinessPubCarInfo", info);

        return businessPubCarInfos;
    }

    @Override
    public List<Map<String, Object>> queryInfoByCarId(String carId) {
        logger.debug("查询车辆管理业务表信息 入参 info : {}", carId);

        List<Map<String, Object>> idInfos = sqlSessionTemplate.selectList("pubCarServiceDaoImpl.queryInfoByCarId", carId);

        return idInfos;
    }

    @Override
    public void saveDelBusinessPubCarInfo(JSONObject businessPubCarInfo) {
        logger.debug("保存车辆管理表信息Business 入参 info : {}", businessPubCarInfo);

        // 保存数据到business_pubcar_car
        int savePubCarFlag = sqlSessionTemplate.insert("pubCarServiceDaoImpl.saveBusinessPubCarInfo", businessPubCarInfo);
        // 保存数据到business_pubcar_car_attr
        int saveAttrFlag = sqlSessionTemplate.insert("pubCarAttrServiceDaoImpl.saveBusinessPubCarAttrInfo", businessPubCarInfo);
        // 保存数据到business_pubcar_car_insurance
        int saveInsuranceFlag = sqlSessionTemplate.insert("pubCarInsuranceServiceDaoImpl.saveBusinessPubCarInsuranceInfo", businessPubCarInfo);

        if (savePubCarFlag < 1 || saveAttrFlag < 1 || saveInsuranceFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆管理表信息Instance数据失败：" + JSONObject.toJSONString(businessPubCarInfo));
        }
    }

    @Override
    public List<ApiSimplePubCarDataVo> querySimplePubCars(Map<String, Object> map) {
        return sqlSessionTemplate.selectList("pubCarServiceDaoImpl.querySimplePubCars", map);
    }

    @Override
    public int queryCarStatisticsCount(PubCarDto pubCarDto) {
        List<Map> businessPubCarInfos = sqlSessionTemplate.selectList("pubCarServiceDaoImpl.queryCarStatisticsCount", pubCarDto);
        if (businessPubCarInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessPubCarInfos.get(0).get("count").toString());
    }

    @Override
    public List<ApiCarStatisticsDataVo> queryCarStatistics(PubCarDto pubCarDto) {
        return sqlSessionTemplate.selectList("pubCarServiceDaoImpl.queryCarStatistics", pubCarDto);
    }

    @Override
    public int queryPubCarArchivesCount(PubCarDto pubCarDto) {
        List<Map> businessPubCarInfos = sqlSessionTemplate.selectList("pubCarServiceDaoImpl.queryPubCarArchivesCount", pubCarDto);
        if (businessPubCarInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessPubCarInfos.get(0).get("count").toString());
    }

    @Override
    public List<ApiPubCarArchivesDataVo> queryPubCarArchives(PubCarDto pubCarDto) {
        return sqlSessionTemplate.selectList("pubCarServiceDaoImpl.queryPubCarArchives", pubCarDto);
    }

    @Override
    public ExistPubCarDto existPubCar(ExistPubCarDto existPubCarDto) {
        return sqlSessionTemplate.selectOne("pubCarServiceDaoImpl.existPubCar", existPubCarDto);
    }

    @Override
    public List<PubCarDto> listNotEmptyCars(Map<String, Object> map) {
        List<PubCarDto> notEmptyCarList = sqlSessionTemplate.selectList("pubCarServiceDaoImpl.listNotEmptyCars", map);
        return notEmptyCarList.stream().filter(t -> StringUtils.isNotBlank(t.getCarId())).collect(Collectors.toList());
    }

    @Override
    public List<ApiSimplePubCarDataVo> listEmptyCarInfos(Map<String, Object> param) {
        return sqlSessionTemplate.selectList("pubCarServiceDaoImpl.listEmptyCarInfos", param);
    }

    @Override
    public List<ApiSimplePubCarDataVo> getCarReturnAndReserveInfo(List<String> emptyCarIds) {
        return sqlSessionTemplate.selectList("pubCarServiceDaoImpl.getCarReturnAndReserveInfo", emptyCarIds);
    }
}

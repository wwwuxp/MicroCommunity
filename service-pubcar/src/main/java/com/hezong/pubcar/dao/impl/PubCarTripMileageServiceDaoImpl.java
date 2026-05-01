package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarTripMileageServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarTripMileage.ApiTripMileageAndFeeDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 行驶里程信息表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarTripMileageServiceDaoImpl")
public class PubCarTripMileageServiceDaoImpl extends BaseServiceDao implements IPubCarTripMileageServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarTripMileageServiceDaoImpl.class);

    /**
     * 保存行驶里程信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarTripMileageInfo(Map info) throws DAOException {
        logger.debug("保存行驶里程信息表信息Instance 入参 info : {}", info);

        // int saveFlag = sqlSessionTemplate.insert("pubCarTripMileageServiceDaoImpl.savePubCarTripMileageInfo", info);
        int saveFlag = sqlSessionTemplate.insert("pubCarTripMileageServiceDaoImpl.savePubCarTripMileageInfoV2", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存行驶里程信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询行驶里程信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarTripMileageInfo(Map info) throws DAOException {
        logger.debug("查询行驶里程信息表信息 入参 info : {}", info);

        List<Map> businessPubCarTripMileageInfos = sqlSessionTemplate.selectList("pubCarTripMileageServiceDaoImpl.getPubCarTripMileageInfo", info);

        return businessPubCarTripMileageInfos;
    }


    /**
     * 修改行驶里程信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarTripMileageInfo(Map info) throws DAOException {
        logger.debug("修改行驶里程信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarTripMileageServiceDaoImpl.updatePubCarTripMileageInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改行驶里程信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询行驶里程信息表数量
     *
     * @param info 行驶里程信息表信息
     * @return 行驶里程信息表数量
     */
    @Override
    public int queryPubCarTripMileagesCount(Map info) {
        logger.debug("查询行驶里程信息表数据 入参 info : {}", info);

        List<Map> businessPubCarTripMileageInfos = sqlSessionTemplate.selectList("pubCarTripMileageServiceDaoImpl.queryPubCarTripMileagesCount", info);
        if (businessPubCarTripMileageInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarTripMileageInfos.get(0).get("count").toString());
    }

    @Override
    public List<PubCarTripMileageDto> queryDriverTrip(PubCarTripMileageDto pubCarTripMileageDto) {
        return sqlSessionTemplate.selectList("pubCarTripMileageServiceDaoImpl.queryDriverTrip", pubCarTripMileageDto);
    }

    @Override
    public void saveBusinessPubCarTripMileageInfo(JSONObject info) {
        logger.debug("保存行驶里程信息表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarTripMileageServiceDaoImpl.saveBusinessPubCarTripMileageInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存行驶里程信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarTripMileageInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarTripMileageServiceDaoImpl.getBusinessPubCarTripMileageInfo", info);
    }

    @Override
    public int queryDriverTripIsInProgress(PubCarTripMileageDto info) {
        List<Map> businessPubCarTripMileageInfos = sqlSessionTemplate.selectList("pubCarTripMileageServiceDaoImpl.queryDriverTripIsInProgress", info);
        if (businessPubCarTripMileageInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarTripMileageInfos.get(0).get("count").toString());
    }

    @Override
    public Double queryEndMileageOfPreviousCar(Map<String, String> info) {
        return sqlSessionTemplate.selectOne("pubCarTripMileageServiceDaoImpl.queryEndMileageOfPreviousCar", info);
    }

    @Override
    public ApiTripMileageAndFeeDataVo getTripMileageAndFee(PubCarTripMileageDto pubCarTripMileageDto) {
        return sqlSessionTemplate.selectOne("pubCarTripMileageServiceDaoImpl.getTripMileageAndFee", pubCarTripMileageDto);
    }

    @Override
    public int updateTripMileageAndFee(PubCarTripMileageDto pubCarTripMileageDto) {
        return sqlSessionTemplate.update("pubCarTripMileageServiceDaoImpl.updateTripMileageAndFee", pubCarTripMileageDto);
    }
}

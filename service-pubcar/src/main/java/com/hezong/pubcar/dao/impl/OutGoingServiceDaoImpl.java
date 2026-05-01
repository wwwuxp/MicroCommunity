package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.outGoing.ApiOutGoingDataVo;
import com.java110.vo.api.outGoing.ApiOutGoingStatisticsDataVo;
import com.java110.vo.api.outGoing.ApiUserOutGoingDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 外出信息表服务 与数据库交互
 * Created by hh
 */
@Service("outGoingServiceDaoImpl")
//@Transactional
public class OutGoingServiceDaoImpl extends BaseServiceDao implements IOutGoingServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OutGoingServiceDaoImpl.class);

    /**
     * 保存外出信息表信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOutGoingInfo(Map info) throws DAOException {
        logger.debug("保存外出信息表信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("outGoingServiceDaoImpl.saveOutGoingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存外出信息表信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询外出信息表信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<ApiOutGoingDataVo> getOutGoingInfo(OutGoingDto info) throws DAOException {
        logger.debug("查询外出信息表信息 入参 info : {}",info);

        return sqlSessionTemplate.selectList("outGoingServiceDaoImpl.getOutGoingInfo",info);
    }


    /**
     * 修改外出信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOutGoingInfo(Map info) throws DAOException {
        logger.debug("修改外出信息表信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("outGoingServiceDaoImpl.updateOutGoingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改外出信息表信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询外出信息表数量
     * @param info 外出信息表信息
     * @return 外出信息表数量
     */
    @Override
    public int queryOutGoingsCount(Map info) {
        logger.debug("查询外出信息表数据 入参 info : {}",info);

        List<Map> businessOutGoingInfos = sqlSessionTemplate.selectList("outGoingServiceDaoImpl.queryOutGoingsCount", info);
        if (businessOutGoingInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOutGoingInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessOutGoingInfo(JSONObject info) {
        int saveFlag = sqlSessionTemplate.insert("outGoingServiceDaoImpl.saveBusinessOutGoingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存外出信息表信息Business数据失败："+ JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessOutGoingInfo(Map info) {
        return sqlSessionTemplate.selectList("outGoingServiceDaoImpl.getBusinessOutGoingInfo",info);
    }

    @Override
    public List<ApiUserOutGoingDataVo> queryUserOutGoings(OutGoingDto outGoingDto) {
        return sqlSessionTemplate.selectList("outGoingServiceDaoImpl.queryUserOutGoings", outGoingDto);
    }

    @Override
    public List<ApiOutGoingStatisticsDataVo> queryOutGoingStatistics(OutGoingDto outGoingDto) {
        return sqlSessionTemplate.selectList("outGoingServiceDaoImpl.queryOutGoingStatistics", outGoingDto);
    }

    @Override
    public int queryOutGoingStatisticsCount(OutGoingDto outGoingDto) {
        return sqlSessionTemplate.selectOne("outGoingServiceDaoImpl.queryOutGoingStatisticsCount", outGoingDto);
    }
}

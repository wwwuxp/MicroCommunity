package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarAlarmServiceDao;
import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.vo.api.pubCarAlarm.ApiPubCarAlarmDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 异常报警信息表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarAlarmServiceDaoImpl")
//@Transactional
public class PubCarAlarmServiceDaoImpl extends BaseServiceDao implements IPubCarAlarmServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarAlarmServiceDaoImpl.class);

    /**
     * 保存异常报警信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarAlarmInfo(Map info) throws DAOException {
        logger.debug("保存异常报警信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarAlarmServiceDaoImpl.savePubCarAlarmInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存异常报警信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询异常报警信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<ApiPubCarAlarmDataVo> getPubCarAlarmInfo(PubCarAlarmDto info) throws DAOException {
        logger.debug("查询异常报警信息表信息 入参 info : {}", info);

        return sqlSessionTemplate.selectList("pubCarAlarmServiceDaoImpl.getPubCarAlarmInfo", info);
    }


    /**
     * 修改异常报警信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarAlarmInfo(Map info) throws DAOException {
        logger.debug("修改异常报警信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarAlarmServiceDaoImpl.updatePubCarAlarmInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改异常报警信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询异常报警信息表数量
     *
     * @param info 异常报警信息表信息
     * @return 异常报警信息表数量
     */
    @Override
    public int queryPubCarAlarmsCount(PubCarAlarmDto info) {
        logger.debug("查询异常报警信息表数据 入参 info : {}", info);

        List<Map> businessPubCarAlarmInfos = sqlSessionTemplate.selectList("pubCarAlarmServiceDaoImpl.queryPubCarAlarmsCount", info);
        if (businessPubCarAlarmInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarAlarmInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessPubCarAlarmInfo(JSONObject info) {
        logger.debug("保存异常报警信息表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarAlarmServiceDaoImpl.saveBusinessPubCarAlarmInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存异常报警信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarAlarmInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarAlarmServiceDaoImpl.getBusinessPubCarAlarmInfo", info);
    }

    @Override
    public List<Map> getPubCarAlarmInstanceInfo(Map<String, String> info) {
        return sqlSessionTemplate.selectList("pubCarAlarmServiceDaoImpl.getPubCarAlarmInstanceInfo", info);
    }

    @Override
    public void savePubCarAlarmBatch(List<PubCarAlarmPo> info) {
        sqlSessionTemplate.insert("pubCarAlarmServiceDaoImpl.savePubCarAlarmBatch", info);
    }
}

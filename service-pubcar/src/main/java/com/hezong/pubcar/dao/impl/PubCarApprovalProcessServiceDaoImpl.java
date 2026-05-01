package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarApprovalProcessServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/12/6
 */
@Service("pubCarApprovalProcessServiceDaoImpl")
public class PubCarApprovalProcessServiceDaoImpl extends BaseServiceDao implements IPubCarApprovalProcessServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarApprovalProcessServiceDaoImpl.class);

    @Override
    public void saveBusinessPubCarApprovalProcessInfo(JSONObject info) {
        logger.debug("保存用车审批表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarApprovalProcessServiceDaoImpl.saveBusinessPubCarApprovalProcessInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车审批表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarApprovalProcessInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarApprovalProcessServiceDaoImpl.getBusinessPubCarApprovalProcessInfo", info);
    }

    @Override
    public void savePubCarApprovalProcessInfo(Map info) {
        logger.debug("保存用车审批表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarApprovalProcessServiceDaoImpl.savePubCarApprovalProcessInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车审批表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getPubCarApprovalProcessInstanceInfo(Map<String, String> info) {
        return sqlSessionTemplate.selectList("pubCarApprovalProcessServiceDaoImpl.getPubCarApprovalProcessInstanceInfo", info);
    }

    @Override
    public void updatePubCarApprovalProcessInfo(Map<String, String> info) {
        logger.debug("修改用车审批流程表信息Instance 入参 info : {}", info);

        try {

            sqlSessionTemplate.update("pubCarApprovalProcessServiceDaoImpl.updatePubCarApprovalProcessInfo", info);

        } catch (Exception e) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改用车审批流程信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<PubCarApprovalProcessDto> getTrips(PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        return sqlSessionTemplate.selectList("pubCarApprovalProcessServiceDaoImpl.getTrips", pubCarApprovalProcessDto);
    }

    @Override
    public int getTripIdsCount(PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        List<String> result = sqlSessionTemplate.selectList("pubCarApprovalProcessServiceDaoImpl.getTripIdsCount", pubCarApprovalProcessDto);
        return result.size();
    }

    @Override
    public int getNotApprovalCount(PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        return sqlSessionTemplate.selectOne("pubCarApprovalProcessServiceDaoImpl.getNotApprovalCount", pubCarApprovalProcessDto);
    }

    @Override
    public Integer hasBureauLeaderApprove(Map<String, Object> map) {
        return sqlSessionTemplate.selectOne("pubCarApprovalProcessServiceDaoImpl.hasBureauLeaderApprove", map);
    }

    @Override
    public int getOutGoingIdsCount(PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        return sqlSessionTemplate.selectOne("pubCarApprovalProcessServiceDaoImpl.getOutGoingIdsCount", pubCarApprovalProcessDto);
    }

    @Override
    public List<String> getOutGoingIds(PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        return sqlSessionTemplate.selectList("pubCarApprovalProcessServiceDaoImpl.getOutGoingIds", pubCarApprovalProcessDto);
    }

    @Override
    public List<PubCarApprovalProcessDto> getTripApprovalProcess(List<String> tripIdList) {
        return sqlSessionTemplate.selectList("pubCarApprovalProcessServiceDaoImpl.getTripApprovalProcess", tripIdList);
    }

    @Override
    public List<Integer> getPubCarApprovalProcess(Map<String, String> map) {
        return sqlSessionTemplate.selectList("pubCarApprovalProcessServiceDaoImpl.getPubCarApprovalProcess", map);
    }
}

package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarDriverServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarDriver.PubCarDriverDto;
import com.java110.dto.pubCarDriver.PubCarExistDriverDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.ConstantUtil;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarDriverDataVo;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarSimpleDriverDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 司机管理表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarDriverServiceDaoImpl")
public class PubCarDriverServiceDaoImpl extends BaseServiceDao implements IPubCarDriverServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarDriverServiceDaoImpl.class);

    /**
     * 保存司机管理表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarDriverInfo(Map info) throws DAOException {
        logger.debug("保存司机管理表信息Instance 入参 info : {}", info);

        int savePubCarDriverFlag = sqlSessionTemplate.insert("pubCarDriverServiceDaoImpl.savePubCarDriverInfo", info);

        if (savePubCarDriverFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存司机管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询司机管理表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarDriverInfo(Map info) throws DAOException {
        logger.debug("查询司机管理表信息 入参 info : {}", info);

        return sqlSessionTemplate.selectList("pubCarDriverServiceDaoImpl.getPubCarDriverInfo", info);
    }


    /**
     * 修改司机管理表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarDriverInfo(Map info) throws DAOException {
        logger.debug("修改司机管理表信息Instance 入参 info : {}", info);

        try {

            sqlSessionTemplate.update("pubCarDriverServiceDaoImpl.updatePubCarDriverInfo", info);

        } catch (Exception e) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改司机管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询司机管理表数量
     *
     * @param info 司机管理表信息
     * @return 司机管理表数量
     */
    @Override
    public int queryPubCarDriversCount(Map info) {
        logger.debug("查询司机管理表数据 入参 info : {}", info);

        List<Map> businessPubCarDriverInfos = sqlSessionTemplate.selectList("pubCarDriverServiceDaoImpl.queryPubCarDriversCount", info);
        if (businessPubCarDriverInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarDriverInfos.get(0).get("count").toString());
    }

    /**
     * 保存司机业务信息
     *
     * @param info
     */
    @Override
    public void saveBusinessPubCarDriverInfo(JSONObject info) {
        logger.debug("保存司机管理表信息business 入参 info : {}", info);

        int savePubCarDriverFlag = sqlSessionTemplate.insert("pubCarDriverServiceDaoImpl.saveBusinessPubCarDriverInfo", info);

        if (savePubCarDriverFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存司机管理表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 获取司机业务表信息
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> getBusinessPubCarDriverInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarDriverServiceDaoImpl.getBusinessPubCarDriverInfo", info);
    }

    @Override
    public String queryPubCarDriversByValidateCondition(String jobNum) {
        return sqlSessionTemplate.selectOne("pubCarDriverServiceDaoImpl.queryPubCarDriversByValidateCondition", jobNum);
    }

    @Override
    public List<ApiPubCarSimpleDriverDataVo> queryPubCarDriverNames() {
        return sqlSessionTemplate.selectList("pubCarDriverServiceDaoImpl.queryPubCarDriverNames");
    }

    @Override
    public Integer existPubCarDriver(PubCarExistDriverDto existDriverDto) {
        return sqlSessionTemplate.selectOne("pubCarDriverServiceDaoImpl.existPubCarDriver", existDriverDto);
    }

    @Override
    public List<ApiPubCarDriverDataVo> queryPubCarDrivers(PubCarDriverDto info) {
        logger.debug("查询司机管理表信息 入参 info : {}", info);

        if (Objects.nonNull(info.getSimpleFlag()) && ConstantUtil.SIMPLE_FLAG_STATICS.equals(info.getSimpleFlag())){
            return sqlSessionTemplate.selectList("pubCarDriverServiceDaoImpl.getPubCarDriverStatisticsInfo", info);
        }

        return sqlSessionTemplate.selectList("pubCarDriverServiceDaoImpl.queryPubCarDrivers", info);
    }
}

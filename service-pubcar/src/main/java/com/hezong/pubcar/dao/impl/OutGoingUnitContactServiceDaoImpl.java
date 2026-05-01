package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingUnitContactServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 外出单位联系人信息表服务 与数据库交互
 * Created by hh
 */
@Service("outGoingUnitContactServiceDaoImpl")
//@Transactional
public class OutGoingUnitContactServiceDaoImpl extends BaseServiceDao implements IOutGoingUnitContactServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OutGoingUnitContactServiceDaoImpl.class);

    /**
     * 保存外出单位联系人信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOutGoingUnitContactInfo(Map info) throws DAOException {
        logger.debug("保存外出单位联系人信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("outGoingUnitContactServiceDaoImpl.saveOutGoingUnitContactInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存外出单位联系人信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询外出单位联系人信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOutGoingUnitContactInfo(Map info) throws DAOException {
        logger.debug("查询外出单位联系人信息表信息 入参 info : {}", info);

        List<Map> businessOutGoingUnitContactInfos = sqlSessionTemplate.selectList("outGoingUnitContactServiceDaoImpl.getOutGoingUnitContactInfo", info);

        return businessOutGoingUnitContactInfos;
    }


    /**
     * 修改外出单位联系人信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOutGoingUnitContactInfo(Map info) throws DAOException {
        logger.debug("修改外出单位联系人信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("outGoingUnitContactServiceDaoImpl.updateOutGoingUnitContactInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改外出单位联系人信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询外出单位联系人信息表数量
     *
     * @param info 外出单位联系人信息表信息
     * @return 外出单位联系人信息表数量
     */
    @Override
    public int queryOutGoingUnitContactsCount(Map info) {
        logger.debug("查询外出单位联系人信息表数据 入参 info : {}", info);

        List<Map> businessOutGoingUnitContactInfos = sqlSessionTemplate.selectList("outGoingUnitContactServiceDaoImpl.queryOutGoingUnitContactsCount", info);
        if (businessOutGoingUnitContactInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOutGoingUnitContactInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessOutGoingUnitContactInfo(JSONObject info) {
        int saveFlag = sqlSessionTemplate.insert("outGoingUnitContactServiceDaoImpl.saveBusinessOutGoingUnitContactInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存外出单位联系人信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessOutGoingUnitContactInfo(Map info) {
        return sqlSessionTemplate.selectList("outGoingUnitContactServiceDaoImpl.getBusinessOutGoingUnitContactInfo", info);
    }
}

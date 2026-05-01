package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IOutGoingPersonnelServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 外出人员信息表服务 与数据库交互
 * Created by hh
 */
@Service("outGoingPersonnelServiceDaoImpl")
//@Transactional
public class OutGoingPersonnelServiceDaoImpl extends BaseServiceDao implements IOutGoingPersonnelServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OutGoingPersonnelServiceDaoImpl.class);

    /**
     * 保存外出人员信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOutGoingPersonnelInfo(Map info) throws DAOException {
        logger.debug("保存外出人员信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("outGoingPersonnelServiceDaoImpl.saveOutGoingPersonnelInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存外出人员信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询外出人员信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOutGoingPersonnelInfo(Map info) throws DAOException {
        logger.debug("查询外出人员信息表信息 入参 info : {}", info);

        List<Map> businessOutGoingPersonnelInfos = sqlSessionTemplate.selectList("outGoingPersonnelServiceDaoImpl.getOutGoingPersonnelInfo", info);

        return businessOutGoingPersonnelInfos;
    }


    /**
     * 修改外出人员信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOutGoingPersonnelInfo(Map info) throws DAOException {
        logger.debug("修改外出人员信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("outGoingPersonnelServiceDaoImpl.updateOutGoingPersonnelInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改外出人员信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询外出人员信息表数量
     *
     * @param info 外出人员信息表信息
     * @return 外出人员信息表数量
     */
    @Override
    public int queryOutGoingPersonnelsCount(Map info) {
        logger.debug("查询外出人员信息表数据 入参 info : {}", info);

        List<Map> businessOutGoingPersonnelInfos = sqlSessionTemplate.selectList("outGoingPersonnelServiceDaoImpl.queryOutGoingPersonnelsCount", info);
        if (businessOutGoingPersonnelInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOutGoingPersonnelInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessOutGoingPersonnelInfo(JSONObject info) {
        int saveFlag = sqlSessionTemplate.insert("outGoingPersonnelServiceDaoImpl.saveBusinessOutGoingPersonnelInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存外出人员信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessOutGoingPersonnelInfo(Map info) {
        return sqlSessionTemplate.selectList("outGoingPersonnelServiceDaoImpl.getBusinessOutGoingPersonnelInfo", info);
    }

    @Override
    public List<OutGoingDto> listOutGoingTimesByUserInfo(UserInfoDto userInfoDto) {
        return sqlSessionTemplate.selectList("outGoingPersonnelServiceDaoImpl.listOutGoingTimesByUserInfo", userInfoDto);
    }

    @Override
    public Integer isOutGoing(UserInfoDto userInfoDto) {
        return sqlSessionTemplate.selectOne("outGoingPersonnelServiceDaoImpl.isOutGoing", userInfoDto);
    }
}

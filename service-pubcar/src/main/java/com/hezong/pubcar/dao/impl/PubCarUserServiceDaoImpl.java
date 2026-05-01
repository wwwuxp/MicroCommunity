package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarUserServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarUser.DepartmentStatisticsDto;
import com.java110.dto.pubCarUser.PubCarExistUserDto;
import com.java110.dto.pubCarUser.PubCarUserDto;
import com.java110.dto.user.UserSimpleDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarUser.ApiDepartmentNamesDataVo;
import com.java110.vo.api.pubCarUser.ApiDepartmentStatisticsDataVo;
import com.java110.vo.api.pubCarUser.ApiPubCarUserDetailDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 用车人管理表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarUserServiceDaoImpl")
public class PubCarUserServiceDaoImpl extends BaseServiceDao implements IPubCarUserServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarUserServiceDaoImpl.class);

    /**
     * 保存用车人管理表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarUserInfo(Map info) throws DAOException {
        logger.debug("保存用车人管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarUserServiceDaoImpl.savePubCarUserInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车人管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询用车人管理表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarUserInfo(Map info) throws DAOException {
        logger.debug("查询用车人管理表信息 入参 info : {}", info);

        List<Map> businessPubCarUserInfos = sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getPubCarUserInfo", info);

        return businessPubCarUserInfos;
    }


    /**
     * 修改用车人管理表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarUserInfo(Map info) throws DAOException {
        logger.debug("修改用车人管理表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarUserServiceDaoImpl.updatePubCarUserInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改用车人管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询用车人管理表数量
     *
     * @param info 用车人管理表信息
     * @return 用车人管理表数量
     */
    @Override
    public int queryPubCarUsersCount(Map info) {
        logger.debug("查询用车人管理表数据 入参 info : {}", info);

        List<Map> businessPubCarUserInfos = sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.queryPubCarUsersCount", info);
        if (businessPubCarUserInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarUserInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessPubCarUserInfo(JSONObject info) {
        logger.debug("保存用车人管理表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarUserServiceDaoImpl.saveBusinessPubCarUserInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车人管理表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarUserInfo(Map info) {
        logger.debug("查询用车人管理表信息 入参 info : {}", info);

        List<Map> businessPubCarUserInfos = sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getBusinessPubCarUserInfo", info);

        return businessPubCarUserInfos;
    }

    @Override
    public List<Map> getPubCarUserInfos(Map info) {
        logger.debug("查询用车人管理表信息 入参 info : {}", info);

        if (Objects.equals(1, info.get("isSelfDriving"))){
            return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getPubCarSelfDrivingUserInfos", info);
        }

        return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getPubCarUserInfos", info);
    }

    @Override
    public int queryPubCarUserDetailsCount(PubCarUserDto info) {
        List<Map> businessPubCarUserDetailInfos;
        Integer isSelfDriving = info.getIsSelfDriving();
        if (isSelfDriving == 0) {
            businessPubCarUserDetailInfos = sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.queryPubCarUserDetailsCount", info);
        } else {
            businessPubCarUserDetailInfos = sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.queryPubCarSelfDrivingUserDetailsCount", info);
        }

        if (businessPubCarUserDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarUserDetailInfos.get(0).get("count").toString());
    }

    @Override
    public List<ApiPubCarUserDetailDataVo> getPubCarUserDetails(PubCarUserDto pubCarUserDto) {
        Integer isSelfDriving = pubCarUserDto.getIsSelfDriving();

        if (isSelfDriving == 0) {
            return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getPubCarUserDetails", pubCarUserDto);
        } else {
            return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getPubCarSelfDrivingUserDetails", pubCarUserDto);
        }
    }

    @Override
    public int queryDepartmentStatisticsCount(DepartmentStatisticsDto info) {
        List<Map> businessPubCarUserDetailInfos = sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.queryDepartmentStatisticsCount", info);
        if (businessPubCarUserDetailInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessPubCarUserDetailInfos.get(0).get("count").toString());
    }

    @Override
    public List<ApiDepartmentStatisticsDataVo> queryDepartmentStatistics(DepartmentStatisticsDto departmentStatisticsDto) {
        return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.queryDepartmentStatistics", departmentStatisticsDto);
    }

    @Override
    public List<ApiDepartmentNamesDataVo> queryDepartmentNames(String storeId) {
        return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.queryDepartmentNames", storeId);
    }

    @Override
    public List<String> listExistPubCarUser(PubCarExistUserDto existUserDto) {
        return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.listExistPubCarUser", existUserDto);
    }

    @Override
    public List<UserSimpleDto> getNameDepartmentPositionInfoBatch(List<String> info) {
        return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getNameDepartmentPositionInfoBatch", info);
    }

    @Override
    public List<Map<String, String>> getUserNameByUserIds(Set<String> userIdSet) {
        return sqlSessionTemplate.selectList("pubCarUserServiceDaoImpl.getUserNameByUserIds", userIdSet);
    }
}

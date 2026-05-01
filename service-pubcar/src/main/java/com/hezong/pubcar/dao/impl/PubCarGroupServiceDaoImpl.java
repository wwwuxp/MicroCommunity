package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarGroupServiceDao;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.vo.api.pubCarGroup.ApiPubCarGroupDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 分组信息表服务 与数据库交互
 * Created by hh
 */
@Service("pubCarGroupServiceDaoImpl")
//@Transactional
public class PubCarGroupServiceDaoImpl extends BaseServiceDao implements IPubCarGroupServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PubCarGroupServiceDaoImpl.class);

    /**
     * 保存分组信息表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePubCarGroupInfo(Map info) throws DAOException {
        logger.debug("保存分组信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarGroupServiceDaoImpl.savePubCarGroupInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存分组信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询分组信息表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPubCarGroupInfo(Map info) throws DAOException {
        logger.debug("查询分组信息表信息 入参 info : {}", info);

        List<Map> businessPubCarGroupInfos = sqlSessionTemplate.selectList("pubCarGroupServiceDaoImpl.getPubCarGroupInfo", info);

        return businessPubCarGroupInfos;
    }


    /**
     * 修改分组信息表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePubCarGroupInfo(Map info) throws DAOException {
        logger.debug("修改分组信息表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("pubCarGroupServiceDaoImpl.updatePubCarGroupInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改分组信息表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询分组信息表数量
     *
     * @param info 分组信息表信息
     * @return 分组信息表数量
     */
    @Override
    public int queryPubCarGroupsCount(PubCarGroupDto info) {
        logger.debug("查询分组信息表数据 入参 info : {}", info);

        List<Map> businessPubCarGroupInfos = sqlSessionTemplate.selectList("pubCarGroupServiceDaoImpl.queryPubCarGroupsCount", info);
        if (businessPubCarGroupInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPubCarGroupInfos.get(0).get("count").toString());
    }

    @Override
    public void saveBusinessPubCarGroupInfo(JSONObject info) {
        logger.debug("保存分组信息表信息Business 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("pubCarGroupServiceDaoImpl.saveBusinessPubCarGroupInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存分组信息表信息Business数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public List<Map> getBusinessPubCarGroupInfo(Map info) {
        return sqlSessionTemplate.selectList("pubCarGroupServiceDaoImpl.getBusinessPubCarGroupInfo", info);
    }

    @Override
    public List<ApiPubCarGroupDataVo> queryPubCarGroupNames(String info) {
        return sqlSessionTemplate.selectList("pubCarGroupServiceDaoImpl.queryPubCarGroupNames", info);
    }

    @Override
    public Integer existPubCarGroup(PubCarGroupDto existGroupDto) {
        return sqlSessionTemplate.selectOne("pubCarGroupServiceDaoImpl.existPubCarGroup", existGroupDto);
    }
}

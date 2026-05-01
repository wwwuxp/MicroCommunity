package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarAccidentServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarAccident.PubCarAccidentDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarAccident.ApiPubCarAccidentDataVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用车事故与数据库交互
 * @author hh
 * @date 2021/10/9
 */
@Service("pubCarAccidentServiceDaoImpl")
public class PubCarAccidentServiceDaoImpl extends BaseServiceDao implements IPubCarAccidentServiceDao {

    @Override
    public void saveBusinessPubCarAccidentInfo(JSONObject info) {
        int saveFlag = sqlSessionTemplate.insert("pubCarAccidentServiceDaoImpl.saveBusinessPubCarAccidentInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车事故业务表数据失败");
        }
    }

    @Override
    public List<Map<String, Object>> getBusinessPubCarAccidentInfo(Map<String, Object> info) {
        return sqlSessionTemplate.selectList("pubCarAccidentServiceDaoImpl.getBusinessPubCarAccidentInfo", info);
    }

    @Override
    public void savePubCarAccidentInfo(Map<String, Object> info) {
        int saveFlag = sqlSessionTemplate.insert("pubCarAccidentServiceDaoImpl.savePubCarAccidentInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车事故实例表数据失败");
        }
    }

    @Override
    public int listPubCarAccidentsCount(PubCarAccidentDto pubCarAccidentDto) {
        return sqlSessionTemplate.selectOne("pubCarAccidentServiceDaoImpl.listPubCarAccidentsCount", pubCarAccidentDto);
    }

    @Override
    public List<ApiPubCarAccidentDataVo> listPubCarAccidents(PubCarAccidentDto pubCarAccidentDto) {
        return sqlSessionTemplate.selectList("pubCarAccidentServiceDaoImpl.listPubCarAccidents", pubCarAccidentDto);
    }
}

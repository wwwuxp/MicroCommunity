package com.hezong.pubcar.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.hezong.pubcar.dao.IPubCarReimburseServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.pubCarReimburse.PubCarReimburseDto;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarReimburse.ApiPubCarReimburseDataVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author hh
 * @date 2021/10/12
 */
@Service("pubCarReimburseServiceDaoImpl")
public class PubCarReimburseServiceDaoImpl extends BaseServiceDao implements IPubCarReimburseServiceDao {

    @Override
    public void saveBusinessPubCarReimburseInfo(JSONObject info) {
        int saveFlag = sqlSessionTemplate.insert("pubCarReimburseServiceDaoImpl.saveBusinessPubCarReimburseInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车报销业务表数据失败");
        }
    }

    @Override
    public List<Map<String, Object>> getBusinessPubCarReimburseInfo(Map<String, Object> info) {
        return sqlSessionTemplate.selectList("pubCarReimburseServiceDaoImpl.getBusinessPubCarReimburseInfo", info);
    }

    @Override
    public void savePubCarReimburseInfo(Map<String, Object> info) {
        int saveFlag = sqlSessionTemplate.insert("pubCarReimburseServiceDaoImpl.savePubCarReimburseInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用车报销实例表数据失败");
        }
    }

    @Override
    public int listPubCarReimbursesCount(PubCarReimburseDto pubCarReimburseDto) {
        return sqlSessionTemplate.selectOne("pubCarReimburseServiceDaoImpl.listPubCarReimbursesCount", pubCarReimburseDto);
    }

    @Override
    public List<ApiPubCarReimburseDataVo> listPubCarReimburses(PubCarReimburseDto pubCarReimburseDto) {
        return sqlSessionTemplate.selectList("pubCarReimburseServiceDaoImpl.listPubCarReimburses", pubCarReimburseDto);
    }

    @Override
    public int auditPubCarReimburses(PubCarReimburseDto pubCarReimburseDto) {
        return sqlSessionTemplate.update("pubCarReimburseServiceDaoImpl.auditPubCarReimburses", pubCarReimburseDto);
    }
}

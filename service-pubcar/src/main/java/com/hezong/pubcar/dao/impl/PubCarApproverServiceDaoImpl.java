package com.hezong.pubcar.dao.impl;

import com.hezong.pubcar.dao.IPubCarApproverServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.org.OrgDto;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.vo.api.pubCarApprover.ApiPubCarApproverDataVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/12/7
 */
@Service("pubCarApproverServiceDaoImpl")
public class PubCarApproverServiceDaoImpl extends BaseServiceDao implements IPubCarApproverServiceDao {

    @Override
    public List<String> getPubCarApprovers(Map<String, Integer> map) {
        return sqlSessionTemplate.selectList("pubCarApproverServiceDaoImpl.getPubCarApprovers", map);
    }

    @Override
    public List<String> getUserLeader(Map<String, Object> map) {
        return sqlSessionTemplate.selectList("pubCarApproverServiceDaoImpl.getUserLeader", map);
    }

    @Override
    public Integer existPubCarApprover(PubCarApproverDto existApproverDto) {
        return sqlSessionTemplate.selectOne("pubCarApproverServiceDaoImpl.existPubCarApprover", existApproverDto);
    }

    @Override
    public int savePubCarApprover(PubCarApproverDto approverDto) {
        return sqlSessionTemplate.insert("pubCarApproverServiceDaoImpl.savePubCarApprover", approverDto);
    }

    @Override
    public int queryPubCarApproversCount(PubCarApproverDto pubCarApproverDto) {
        return sqlSessionTemplate.selectOne("pubCarApproverServiceDaoImpl.queryPubCarApproversCount", pubCarApproverDto);
    }

    @Override
    public List<ApiPubCarApproverDataVo> queryPubCarApprovers(PubCarApproverDto pubCarApproverDto) {
        return sqlSessionTemplate.selectList("pubCarApproverServiceDaoImpl.queryPubCarApprovers", pubCarApproverDto);
    }

    @Override
    public int deleteById(PubCarApproverDto pubCarApproverDto) {
        return sqlSessionTemplate.update("pubCarApproverServiceDaoImpl.deleteById", pubCarApproverDto);
    }

    @Override
    public Integer isApprover(Map<String, Object> map) {
        return sqlSessionTemplate.selectOne("pubCarApproverServiceDaoImpl.isApprover", map);
    }

    @Override
    public PubCarApproverDto getRoleTypeAndDepartmentIdByApproverId(Map<String, Object> map) {
        return sqlSessionTemplate.selectOne("pubCarApproverServiceDaoImpl.getRoleTypeAndDepartmentIdByApproverId", map);
    }

    @Override
    public List<PubCarApproverDto> getAllBureauLeaderDepartmentId() {
        return sqlSessionTemplate.selectList("pubCarApproverServiceDaoImpl.getAllBureauLeaderDepartmentId");
    }

    @Override
    public List<OrgDto> getOrgNameByOrgIds(List<String> idList) {
        return sqlSessionTemplate.selectList("pubCarApproverServiceDaoImpl.getOrgNameByOrgIds", idList);
    }

    @Override
    public List<PubCarApproverDto> getPubCarAdministrators() {
        return sqlSessionTemplate.selectList("pubCarApproverServiceDaoImpl.getPubCarAdministrators");
    }

    @Override
    public int updatePubCarApprover(PubCarApproverDto approverDto) {
        return sqlSessionTemplate.update("pubCarApproverServiceDaoImpl.updateById", approverDto);
    }

}

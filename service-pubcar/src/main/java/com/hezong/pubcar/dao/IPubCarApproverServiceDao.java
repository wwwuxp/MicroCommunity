package com.hezong.pubcar.dao;

import com.java110.dto.org.OrgDto;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.vo.api.pubCarApprover.ApiPubCarApproverDataVo;

import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @date 2021/12/7
 */
public interface IPubCarApproverServiceDao {

    /**
     * 获取用车管理人信息
     * @return List
     */
    List<String> getPubCarApprovers(Map<String, Integer> map);

    /**
     * 获取用户领导
     * @param map 请求参数
     * @return List
     */
    List<String> getUserLeader(Map<String, Object> map);

    /**
     * 是否存在相同公车审批人
     * @param existApproverDto 请求参数
     * @return Integer
     */
    Integer existPubCarApprover(PubCarApproverDto existApproverDto);

    /**
     * 保存用车审批人信息
     * @param approverDto 请求参数
     * @return int
     */
    int savePubCarApprover(PubCarApproverDto approverDto);

    /**
     * 获取用车审批人数量
     * @param pubCarApproverDto 请求参数
     * @return int
     */
    int queryPubCarApproversCount(PubCarApproverDto pubCarApproverDto);

    /**
     * 获取用车审批人列表
     * @param pubCarApproverDto 请求参数
     * @return list
     */
    List<ApiPubCarApproverDataVo> queryPubCarApprovers(PubCarApproverDto pubCarApproverDto);

    /**
     * 根据id删除用车审批人信息
     * @param pubCarApproverDto 参数
     * @return int
     */
    int deleteById(PubCarApproverDto pubCarApproverDto);

    /**
     * 是否使用车审批人
     * @param map 请求参数
     */
    Integer isApprover(Map<String, Object> map);

    /**
     * 根据审核人id获取角色类型
     * @param map 请求参数
     * @return PubCarApproverDto
     */
    PubCarApproverDto getRoleTypeAndDepartmentIdByApproverId(Map<String, Object> map);

    /**
     * 获取roleType=2的所有部门id
     * @return List
     */
    List<PubCarApproverDto> getAllBureauLeaderDepartmentId();

    /**
     * 获取部门名字
     * @param idList id集合
     * @return List
     */
    List<OrgDto> getOrgNameByOrgIds(List<String> idList);

    /**
     * 获取用车管理员信息
     * @return list
     */
    List<PubCarApproverDto> getPubCarAdministrators();

    /**
     * 更新审批人
     * @param approverDto approverDto
     * @return int
     */
    int updatePubCarApprover(PubCarApproverDto approverDto);

}

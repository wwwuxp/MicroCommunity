package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarApproverServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.org.OrgDto;
import com.java110.dto.pubCarApprover.PubCarApproverDto;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.po.pubCarApprover.PubCarApproverPo;
import com.java110.vo.api.pubCarApprover.ApiPubCarApproverDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hh
 * @date 2021/12/7
 */
@RestController
public class PubCarApproverInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarApproverInnerServiceSMO {

    @Autowired
    private IPubCarApproverServiceDao pubCarApproverServiceDaoImpl;

    @Override
    public List<String> getPubCarApprovers(Integer roleType, Integer tableType) {
        Map<String, Integer> map = new HashMap<>();
        map.put("roleType", roleType);
        map.put("tableType", tableType);
        return pubCarApproverServiceDaoImpl.getPubCarApprovers(map);
    }

    @Override
    public List<String> getUserLeader(String userId, Integer tableType) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("tableType", tableType);
        if (PubCarApproverPo.TYPE_OUTGOING.equals(tableType)){
            map.put("flag", 1);
        }

        return pubCarApproverServiceDaoImpl.getUserLeader(map);
    }

    @Override
    public boolean existPubCarApprover(@RequestBody PubCarApproverDto existApproverDto) {
        Integer exist = pubCarApproverServiceDaoImpl.existPubCarApprover(existApproverDto);
        return Objects.nonNull(exist);
    }

    @Override
    public int savePubCarApprover(@RequestBody PubCarApproverDto approverDto) {
        List<String> approverIds = approverDto.getApproverIds();
        for (String approverId : approverIds) {
            PubCarApproverDto pubCarApproverDto = new PubCarApproverDto();
            pubCarApproverDto.setApproverId(approverId);
            Integer roleType = approverDto.getRoleType();
            Integer tableType = approverDto.getTableType();
            pubCarApproverDto.setRoleType(roleType);
            pubCarApproverDto.setTableType(tableType);

            if (Objects.equals(tableType, PubCarApproverPo.TYPE_PUBCAR)){
                // 用车
                if (Objects.equals(roleType, PubCarApproverPo.PUBCAR_BUREAU_LEADER) || Objects.equals(roleType, PubCarApproverPo.PUBCAR_ADMINISTRATOR)){
                    pubCarApproverDto.setDepartmentId(null);
                }else {
                    pubCarApproverDto.setDepartmentId(approverDto.getDepartmentId());
                }
            }else if (Objects.equals(tableType, PubCarApproverPo.TYPE_OUTGOING)){
                // 外出
                if (Objects.equals(roleType, PubCarApproverPo.OUTGOING_BUREAU_HEADER)){
                    pubCarApproverDto.setDepartmentId(null);
                }else {
                    pubCarApproverDto.setDepartmentId(approverDto.getDepartmentId());
                }
            }

            pubCarApproverDto.setCommunityId(approverDto.getCommunityId());
            pubCarApproverServiceDaoImpl.savePubCarApprover(pubCarApproverDto);
        }
        return 1;
    }

    @Override
    public int queryPubCarApproversCount(@RequestBody PubCarApproverDto pubCarApproverDto) {
        return pubCarApproverServiceDaoImpl.queryPubCarApproversCount(pubCarApproverDto);
    }

    @Override
    public List<ApiPubCarApproverDataVo> queryPubCarApprovers(@RequestBody PubCarApproverDto pubCarApproverDto) {
        //校验是否传了 分页信息
        int page = pubCarApproverDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            pubCarApproverDto.setPage((page - 1) * pubCarApproverDto.getRow());
        }
        Set<String> idList = new HashSet<>();
        List<ApiPubCarApproverDataVo> pubCarApproverDataVos = pubCarApproverServiceDaoImpl.queryPubCarApprovers(pubCarApproverDto);
        for (ApiPubCarApproverDataVo pubCarApproverDataVo : pubCarApproverDataVos) {
            String approvalRange = pubCarApproverDataVo.getApprovalRange();
            if (StringUtils.isNotBlank(approvalRange)){
                String[] departmentIdSplit = approvalRange.split(",");
                idList.addAll(Arrays.asList(departmentIdSplit));
            }
        }

        // 填充审核范围
        if (CollectionUtils.isNotEmpty(idList)){
            List<OrgDto> orgDtoList = pubCarApproverServiceDaoImpl.getOrgNameByOrgIds(new ArrayList<>(idList));
            Map<String, String> orgIdAndOrgNameMap = orgDtoList.stream().collect(Collectors.toMap(OrgDto::getOrgId, OrgDto::getOrgName, (k1, k2) -> k2));
            for (ApiPubCarApproverDataVo pubCarApproverDataVo : pubCarApproverDataVos) {
                String approvalRange = pubCarApproverDataVo.getApprovalRange();
                StringJoiner sj = new StringJoiner(",");
                if (StringUtils.isNotBlank(approvalRange)){
                    String[] departmentIdSplit = approvalRange.split(",");
                    for (String departmentId : departmentIdSplit) {
                        String departmentName = orgIdAndOrgNameMap.get(departmentId);
                        sj.add(departmentName);
                    }
                }else {
                    // 设置为全局
                    sj.add("全局");
                }
                pubCarApproverDataVo.setApprovalRange(sj.toString());
            }
        }
        return pubCarApproverDataVos;
    }

    @Override
    public int deleteById(@RequestBody PubCarApproverDto pubCarApproverDto) {
        return pubCarApproverServiceDaoImpl.deleteById(pubCarApproverDto);
    }

    @Override
    public boolean isApprover(String userId, Integer tableType) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("tableType", tableType);
        Integer b = pubCarApproverServiceDaoImpl.isApprover(map);
        return Objects.nonNull(b);
    }

    @Override
    public PubCarApproverDto getRoleTypeAndDepartmentIdByApproverId(String userId, Integer tableType) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("tableType", tableType);
        return pubCarApproverServiceDaoImpl.getRoleTypeAndDepartmentIdByApproverId(map);
    }

    @Override
    public List<String> whichBureauLeaderManage(String departmentId) {
        HashSet<String> resultSet = new HashSet<>();
        // 获取所有局领导(roleType=2)管辖的部门id
        List<PubCarApproverDto> pubCarApproverDtos = pubCarApproverServiceDaoImpl.getAllBureauLeaderDepartmentId();
        for (PubCarApproverDto pubCarApproverDto : pubCarApproverDtos) {
            String approverId = pubCarApproverDto.getApproverId();
            String tempDepartmentIdStr = pubCarApproverDto.getDepartmentId();

            String[] departmentIdSplit = tempDepartmentIdStr.split(",");
            if (ArrayUtils.isEmpty(departmentIdSplit)){
                continue;
            }
            for (String oneDepartmentId : departmentIdSplit) {
                if (Objects.equals(departmentId, oneDepartmentId)){
                    resultSet.add(approverId);
                }
            }
        }

        return new ArrayList<>(resultSet);
    }

    @Override
    public int updatePubCarApprover(@RequestBody PubCarApproverDto approverDto) {
        return pubCarApproverServiceDaoImpl.updatePubCarApprover(approverDto);
    }

}

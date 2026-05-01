package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarApprovalProcessServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;
import com.java110.intf.pubcar.IPubCarApprovalProcessInnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hh
 * @date 2021/12/7
 */
@RestController
public class PubCarApprovalProcessInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarApprovalProcessInnerServiceSMO {

    @Autowired
    private IPubCarApprovalProcessServiceDao pubCarApprovalProcessServiceDaoImpl;

    @Override
    public int getTripIdsCount(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        return pubCarApprovalProcessServiceDaoImpl.getTripIdsCount(pubCarApprovalProcessDto);
    }

    @Override
    public List<PubCarApprovalProcessDto> getTrips(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        //校验是否传了 分页信息
        int page = pubCarApprovalProcessDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            pubCarApprovalProcessDto.setPage((page - 1) * pubCarApprovalProcessDto.getRow());
        }

        return pubCarApprovalProcessServiceDaoImpl.getTrips(pubCarApprovalProcessDto);
    }

    @Override
    public int getNotApprovalCount(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        return pubCarApprovalProcessServiceDaoImpl.getNotApprovalCount(pubCarApprovalProcessDto);
    }

    @Override
    public boolean hasBureauLeaderApprove(String tripId, String communityId) {
        Map<String, Object> map = new HashMap<>();
        map.put("tripId", tripId);
        map.put("communityId", communityId);
        Integer has = pubCarApprovalProcessServiceDaoImpl.hasBureauLeaderApprove(map);
        return Objects.nonNull(has);
    }

    @Override
    public int getOutGoingIdsCount(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        return pubCarApprovalProcessServiceDaoImpl.getOutGoingIdsCount(pubCarApprovalProcessDto);
    }

    @Override
    public List<String> getOutGoingIds(@RequestBody PubCarApprovalProcessDto pubCarApprovalProcessDto) {
        //校验是否传了 分页信息
        int page = pubCarApprovalProcessDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            pubCarApprovalProcessDto.setPage((page - 1) * pubCarApprovalProcessDto.getRow());
        }
        return pubCarApprovalProcessServiceDaoImpl.getOutGoingIds(pubCarApprovalProcessDto);
    }

    @Override
    public List<Integer> getPubCarApprovalProcess(String tripId) {
        Map<String, String> map = new HashMap<>();
        map.put("tripId", tripId);
        return pubCarApprovalProcessServiceDaoImpl.getPubCarApprovalProcess(map);
    }
}

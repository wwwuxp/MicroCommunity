package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarGroupServiceDao;
import com.java110.intf.pubcar.IPubCarGroupInnerServiceSMO;
import com.java110.po.pubCarGroup.PubCarGroupPo;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.vo.api.pubCarGroup.ApiPubCarGroupDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 分组信息表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarGroupInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarGroupInnerServiceSMO {

    @Autowired
    private IPubCarGroupServiceDao pubCarGroupServiceDaoImpl;


    @Override
    public int savePubCarGroup(@RequestBody PubCarGroupPo pubCarGroupPo) {
        int saveFlag = 1;
        pubCarGroupServiceDaoImpl.savePubCarGroupInfo(BeanConvertUtil.beanCovertMap(pubCarGroupPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarGroup(@RequestBody PubCarGroupPo pubCarGroupPo) {
        int saveFlag = 1;
        pubCarGroupServiceDaoImpl.updatePubCarGroupInfo(BeanConvertUtil.beanCovertMap(pubCarGroupPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarGroup(@RequestBody PubCarGroupPo pubCarGroupPo) {
        int saveFlag = 1;
        pubCarGroupPo.setStatusCd("1");
        pubCarGroupServiceDaoImpl.updatePubCarGroupInfo(BeanConvertUtil.beanCovertMap(pubCarGroupPo));
        return saveFlag;
    }

    @Override
    public List<PubCarGroupDto> queryPubCarGroups(@RequestBody PubCarGroupDto pubCarGroupDto) {

        //校验是否传了 分页信息

        int page = pubCarGroupDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarGroupDto.setPage((page - 1) * pubCarGroupDto.getRow());
        }

        List<PubCarGroupDto> pubCarGroups = BeanConvertUtil.covertBeanList(pubCarGroupServiceDaoImpl.getPubCarGroupInfo(BeanConvertUtil.beanCovertMap(pubCarGroupDto)), PubCarGroupDto.class);

        return pubCarGroups;
    }


    @Override
    public int queryPubCarGroupsCount(@RequestBody PubCarGroupDto pubCarGroupDto) {
        return pubCarGroupServiceDaoImpl.queryPubCarGroupsCount(pubCarGroupDto);
    }

    @Override
    public List<ApiPubCarGroupDataVo> queryPubCarGroupNames(String communityId) {
        return pubCarGroupServiceDaoImpl.queryPubCarGroupNames(communityId);
    }

    @Override
    public boolean existPubCarGroup(@RequestBody PubCarGroupDto existGroupDto) {
        return Objects.nonNull(pubCarGroupServiceDaoImpl.existPubCarGroup(existGroupDto));
    }
}

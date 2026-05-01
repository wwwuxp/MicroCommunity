package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarUserServiceDao;
import com.java110.dto.pubCarUser.DepartmentStatisticsDto;
import com.java110.dto.pubCarUser.PubCarExistUserDto;
import com.java110.dto.pubCarUser.PubCarUserDto;
import com.java110.intf.pubcar.IPubCarUserInnerServiceSMO;
import com.java110.po.pubCarUser.PubCarUserPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.vo.api.pubCarUser.ApiDepartmentNamesDataVo;
import com.java110.vo.api.pubCarUser.ApiDepartmentStatisticsDataVo;
import com.java110.vo.api.pubCarUser.ApiPubCarUserDetailDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 用车人管理表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarUserInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarUserInnerServiceSMO {

    @Autowired
    private IPubCarUserServiceDao pubCarUserServiceDaoImpl;


    @Override
    public int savePubCarUser(@RequestBody PubCarUserPo pubCarUserPo) {
        int saveFlag = 1;
        pubCarUserServiceDaoImpl.savePubCarUserInfo(BeanConvertUtil.beanCovertMap(pubCarUserPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarUser(@RequestBody PubCarUserPo pubCarUserPo) {
        int saveFlag = 1;
        pubCarUserServiceDaoImpl.updatePubCarUserInfo(BeanConvertUtil.beanCovertMap(pubCarUserPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarUser(@RequestBody PubCarUserPo pubCarUserPo) {
        int saveFlag = 1;
        pubCarUserPo.setStatusCd("1");
        pubCarUserServiceDaoImpl.updatePubCarUserInfo(BeanConvertUtil.beanCovertMap(pubCarUserPo));
        return saveFlag;
    }

    @Override
    public List<PubCarUserDto> queryPubCarUsers(@RequestBody PubCarUserDto pubCarUserDto) {

        //校验是否传了 分页信息

        int page = pubCarUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarUserDto.setPage((page - 1) * pubCarUserDto.getRow());
        }

        return BeanConvertUtil.covertBeanList(pubCarUserServiceDaoImpl.getPubCarUserInfos(BeanConvertUtil.beanCovertMap(pubCarUserDto)), PubCarUserDto.class);
    }


    @Override
    public int queryPubCarUsersCount(@RequestBody PubCarUserDto pubCarUserDto) {
        return pubCarUserServiceDaoImpl.queryPubCarUsersCount(BeanConvertUtil.beanCovertMap(pubCarUserDto));
    }

    @Override
    public int queryPubCarUserDetailsCount(@RequestBody PubCarUserDto pubCarUserDto) {
        return pubCarUserServiceDaoImpl.queryPubCarUserDetailsCount(pubCarUserDto);
    }

    @Override
    public List<ApiPubCarUserDetailDataVo> queryPubCarUserDetails(@RequestBody PubCarUserDto pubCarUserDto) {
        //校验是否传了 分页信息

        int page = pubCarUserDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarUserDto.setPage((page - 1) * pubCarUserDto.getRow());
        }

        return pubCarUserServiceDaoImpl.getPubCarUserDetails(pubCarUserDto);
    }

    @Override
    public int queryDepartmentStatisticsCount(@RequestBody DepartmentStatisticsDto departmentStatisticsDto) {
        return pubCarUserServiceDaoImpl.queryDepartmentStatisticsCount(departmentStatisticsDto);
    }

    @Override
    public List<ApiDepartmentStatisticsDataVo> queryDepartmentStatistics(@RequestBody DepartmentStatisticsDto departmentStatisticsDto) {
        int page = departmentStatisticsDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            departmentStatisticsDto.setPage((page - 1) * departmentStatisticsDto.getRow());
        }

        return pubCarUserServiceDaoImpl.queryDepartmentStatistics(departmentStatisticsDto);
    }

    @Override
    public List<ApiDepartmentNamesDataVo> queryDepartmentNames(String storeId) {
        return pubCarUserServiceDaoImpl.queryDepartmentNames(storeId);
    }

    @Override
    public List<String> listExistPubCarUser(@RequestBody PubCarExistUserDto existUserDto) {
        return pubCarUserServiceDaoImpl.listExistPubCarUser(existUserDto);
    }
}

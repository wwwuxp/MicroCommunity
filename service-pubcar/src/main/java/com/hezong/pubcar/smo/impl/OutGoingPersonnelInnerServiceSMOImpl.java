package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IOutGoingPersonnelServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.UserInfoDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.dto.outGoingPersonnel.OutGoingPersonnelDto;
import com.java110.intf.pubcar.IOutGoingPersonnelInnerServiceSMO;
import com.java110.po.outGoingPersonnel.OutGoingPersonnelPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 外出人员信息表内部服务实现类
 * add by hh
 **/
@RestController
public class OutGoingPersonnelInnerServiceSMOImpl extends BaseServiceSMO implements IOutGoingPersonnelInnerServiceSMO {

    @Autowired
    private IOutGoingPersonnelServiceDao outGoingPersonnelServiceDaoImpl;


    @Override
    public int saveOutGoingPersonnel(@RequestBody OutGoingPersonnelPo outGoingPersonnelPo) {
        int saveFlag = 1;
        outGoingPersonnelServiceDaoImpl.saveOutGoingPersonnelInfo(BeanConvertUtil.beanCovertMap(outGoingPersonnelPo));
        return saveFlag;
    }

    @Override
    public int updateOutGoingPersonnel(@RequestBody OutGoingPersonnelPo outGoingPersonnelPo) {
        int saveFlag = 1;
        outGoingPersonnelServiceDaoImpl.updateOutGoingPersonnelInfo(BeanConvertUtil.beanCovertMap(outGoingPersonnelPo));
        return saveFlag;
    }

    @Override
    public int deleteOutGoingPersonnel(@RequestBody OutGoingPersonnelPo outGoingPersonnelPo) {
        int saveFlag = 1;
        outGoingPersonnelPo.setStatusCd("1");
        outGoingPersonnelServiceDaoImpl.updateOutGoingPersonnelInfo(BeanConvertUtil.beanCovertMap(outGoingPersonnelPo));
        return saveFlag;
    }

    @Override
    public List<OutGoingPersonnelDto> queryOutGoingPersonnels(@RequestBody OutGoingPersonnelDto outGoingPersonnelDto) {

        //校验是否传了 分页信息

        int page = outGoingPersonnelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            outGoingPersonnelDto.setPage((page - 1) * outGoingPersonnelDto.getRow());
        }

        List<OutGoingPersonnelDto> outGoingPersonnels = BeanConvertUtil.covertBeanList(outGoingPersonnelServiceDaoImpl.getOutGoingPersonnelInfo(BeanConvertUtil.beanCovertMap(outGoingPersonnelDto)), OutGoingPersonnelDto.class);

        return outGoingPersonnels;
    }


    @Override
    public int queryOutGoingPersonnelsCount(@RequestBody OutGoingPersonnelDto outGoingPersonnelDto) {
        return outGoingPersonnelServiceDaoImpl.queryOutGoingPersonnelsCount(BeanConvertUtil.beanCovertMap(outGoingPersonnelDto));
    }

    @Override
    public List<OutGoingDto> listOutGoingTimesByUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return outGoingPersonnelServiceDaoImpl.listOutGoingTimesByUserInfo(userInfoDto);
    }

    @Override
    public boolean isOutGoing(@RequestBody UserInfoDto userInfoDto) {
        Integer exist = outGoingPersonnelServiceDaoImpl.isOutGoing(userInfoDto);
        return Objects.nonNull(exist);
    }
}

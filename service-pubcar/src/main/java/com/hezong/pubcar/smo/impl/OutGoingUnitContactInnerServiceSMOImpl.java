package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IOutGoingUnitContactServiceDao;
import com.java110.intf.pubcar.IOutGoingUnitContactInnerServiceSMO;
import com.java110.po.outGoingUnitContact.OutGoingUnitContactPo;
import com.java110.dto.outGoingUnitContact.OutGoingUnitContactDto;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 外出单位联系人信息表内部服务实现类
 * add by hh
 **/
@RestController
public class OutGoingUnitContactInnerServiceSMOImpl extends BaseServiceSMO implements IOutGoingUnitContactInnerServiceSMO {

    @Autowired
    private IOutGoingUnitContactServiceDao outGoingUnitContactServiceDaoImpl;


    @Override
    public int saveOutGoingUnitContact(@RequestBody OutGoingUnitContactPo outGoingUnitContactPo) {
        int saveFlag = 1;
        outGoingUnitContactServiceDaoImpl.saveOutGoingUnitContactInfo(BeanConvertUtil.beanCovertMap(outGoingUnitContactPo));
        return saveFlag;
    }

     @Override
    public int updateOutGoingUnitContact(@RequestBody  OutGoingUnitContactPo outGoingUnitContactPo) {
        int saveFlag = 1;
         outGoingUnitContactServiceDaoImpl.updateOutGoingUnitContactInfo(BeanConvertUtil.beanCovertMap(outGoingUnitContactPo));
        return saveFlag;
    }

     @Override
    public int deleteOutGoingUnitContact(@RequestBody  OutGoingUnitContactPo outGoingUnitContactPo) {
        int saveFlag = 1;
        outGoingUnitContactPo.setStatusCd("1");
        outGoingUnitContactServiceDaoImpl.updateOutGoingUnitContactInfo(BeanConvertUtil.beanCovertMap(outGoingUnitContactPo));
        return saveFlag;
    }

    @Override
    public List<OutGoingUnitContactDto> queryOutGoingUnitContacts(@RequestBody  OutGoingUnitContactDto outGoingUnitContactDto) {

        //校验是否传了 分页信息

        int page = outGoingUnitContactDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            outGoingUnitContactDto.setPage((page - 1) * outGoingUnitContactDto.getRow());
        }

        List<OutGoingUnitContactDto> outGoingUnitContacts = BeanConvertUtil.covertBeanList(outGoingUnitContactServiceDaoImpl.getOutGoingUnitContactInfo(BeanConvertUtil.beanCovertMap(outGoingUnitContactDto)), OutGoingUnitContactDto.class);

        return outGoingUnitContacts;
    }


    @Override
    public int queryOutGoingUnitContactsCount(@RequestBody OutGoingUnitContactDto outGoingUnitContactDto) {
        return outGoingUnitContactServiceDaoImpl.queryOutGoingUnitContactsCount(BeanConvertUtil.beanCovertMap(outGoingUnitContactDto));    }

}

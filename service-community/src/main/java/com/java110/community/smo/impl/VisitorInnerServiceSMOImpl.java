package com.java110.community.smo.impl;

import com.java110.community.dao.IVisitorServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.visitor.VisitorDto;
import com.java110.intf.community.IVisitorInnerServiceSMO;
import com.java110.po.visitor.VisitorPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.visitInvitation.ApiVisitInvitationDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 访客邀约人信息表内部服务实现类
 * add by hh
 **/
@RestController
public class VisitorInnerServiceSMOImpl extends BaseServiceSMO implements IVisitorInnerServiceSMO {

    @Autowired
    private IVisitorServiceDao visitorServiceDaoImpl;


    @Override
    public int saveVisitor(@RequestBody VisitorPo visitorPo) {
        int saveFlag = 1;
        visitorServiceDaoImpl.saveVisitorInfo(BeanConvertUtil.beanCovertMap(visitorPo));
        return saveFlag;
    }

    @Override
    public int updateVisitor(@RequestBody VisitorPo visitorPo) {
        int saveFlag = 1;
        visitorServiceDaoImpl.updateVisitorInfo(BeanConvertUtil.beanCovertMap(visitorPo));
        return saveFlag;
    }

    @Override
    public int deleteVisitor(@RequestBody VisitorPo visitorPo) {
        int saveFlag = 1;
        visitorPo.setStatusCd("1");
        visitorServiceDaoImpl.updateVisitorInfo(BeanConvertUtil.beanCovertMap(visitorPo));
        return saveFlag;
    }

    @Override
    public List<ApiVisitInvitationDataVo> queryVisitors(@RequestBody VisitorDto visitorDto) {

        //校验是否传了 分页信息

        int page = visitorDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            visitorDto.setPage((page - 1) * visitorDto.getRow());
        }

        return visitorServiceDaoImpl.listVisitors(visitorDto);
    }


    @Override
    public int queryVisitorsCount(@RequestBody VisitorDto visitorDto) {
        return visitorServiceDaoImpl.queryVisitorsCount(BeanConvertUtil.beanCovertMap(visitorDto));
    }

}

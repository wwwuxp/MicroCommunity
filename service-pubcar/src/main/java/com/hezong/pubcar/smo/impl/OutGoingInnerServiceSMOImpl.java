package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IOutGoingServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.outGoing.OutGoingDto;
import com.java110.intf.pubcar.IOutGoingInnerServiceSMO;
import com.java110.po.outGoing.OutGoingPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.outGoing.ApiOutGoingDataVo;
import com.java110.vo.api.outGoing.ApiOutGoingStatisticsDataVo;
import com.java110.vo.api.outGoing.ApiUserOutGoingDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 外出信息表内部服务实现类
 * add by hh
 **/
@RestController
public class OutGoingInnerServiceSMOImpl extends BaseServiceSMO implements IOutGoingInnerServiceSMO {

    @Autowired
    private IOutGoingServiceDao outGoingServiceDaoImpl;


    @Override
    public int saveOutGoing(@RequestBody OutGoingPo outGoingPo) {
        int saveFlag = 1;
        outGoingServiceDaoImpl.saveOutGoingInfo(BeanConvertUtil.beanCovertMap(outGoingPo));
        return saveFlag;
    }

    @Override
    public int updateOutGoing(@RequestBody OutGoingPo outGoingPo) {
        int saveFlag = 1;
        outGoingServiceDaoImpl.updateOutGoingInfo(BeanConvertUtil.beanCovertMap(outGoingPo));
        return saveFlag;
    }

    @Override
    public int deleteOutGoing(@RequestBody OutGoingPo outGoingPo) {
        int saveFlag = 1;
        outGoingPo.setStatusCd("1");
        outGoingServiceDaoImpl.updateOutGoingInfo(BeanConvertUtil.beanCovertMap(outGoingPo));
        return saveFlag;
    }

    @Override
    public List<ApiOutGoingDataVo> queryOutGoings(@RequestBody OutGoingDto outGoingDto) {

        //校验是否传了 分页信息

        int page = outGoingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            outGoingDto.setPage((page - 1) * outGoingDto.getRow());
        }

        return outGoingServiceDaoImpl.getOutGoingInfo(outGoingDto);
    }


    @Override
    public int queryOutGoingsCount(@RequestBody OutGoingDto outGoingDto) {
        return outGoingServiceDaoImpl.queryOutGoingsCount(BeanConvertUtil.beanCovertMap(outGoingDto));
    }

    @Override
    public List<ApiUserOutGoingDataVo> queryUserOutGoings(@RequestBody OutGoingDto outGoingDto) {
        //校验是否传了 分页信息
        int page = outGoingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            outGoingDto.setPage((page - 1) * outGoingDto.getRow());
        }
        return outGoingServiceDaoImpl.queryUserOutGoings(outGoingDto);
    }

    @Override
    public List<ApiOutGoingStatisticsDataVo> queryOutGoingStatistics(@RequestBody OutGoingDto outGoingDto) {
        //校验是否传了 分页信息
        int page = outGoingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            outGoingDto.setPage((page - 1) * outGoingDto.getRow());
        }
        return outGoingServiceDaoImpl.queryOutGoingStatistics(outGoingDto);
    }

    @Override
    public int queryOutGoingStatisticsCount(@RequestBody OutGoingDto outGoingDto) {
        return outGoingServiceDaoImpl.queryOutGoingStatisticsCount(outGoingDto);
    }
}

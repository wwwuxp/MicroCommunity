package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarDriverServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarDriver.PubCarDriverDto;
import com.java110.dto.pubCarDriver.PubCarExistDriverDto;
import com.java110.intf.pubcar.IPubCarDriverInnerServiceSMO;
import com.java110.po.pubCarDriver.PubCarDriverPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarDriverDataVo;
import com.java110.vo.api.pubCarTripDriver.ApiPubCarSimpleDriverDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 用车人管理表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarDriverInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarDriverInnerServiceSMO {

    @Autowired
    private IPubCarDriverServiceDao pubCarDriverServiceDaoImpl;

    @Override
    public int savePubCarDriver(@RequestBody PubCarDriverPo pubCarDriverPo) {
        int saveFlag = 1;
        pubCarDriverServiceDaoImpl.savePubCarDriverInfo(BeanConvertUtil.beanCovertMap(pubCarDriverPo));
        return saveFlag;
    }

     @Override
    public int updatePubCarDriver(@RequestBody PubCarDriverPo pubCarDriverPo) {
        int saveFlag = 1;
         pubCarDriverServiceDaoImpl.updatePubCarDriverInfo(BeanConvertUtil.beanCovertMap(pubCarDriverPo));
        return saveFlag;
    }

     @Override
    public int deletePubCarDriver(@RequestBody PubCarDriverPo pubCarDriverPo) {
        int saveFlag = 1;
        pubCarDriverPo.setStatusCd("1");
        pubCarDriverServiceDaoImpl.updatePubCarDriverInfo(BeanConvertUtil.beanCovertMap(pubCarDriverPo));
        return saveFlag;
    }

    @Override
    public List<ApiPubCarDriverDataVo> queryPubCarDrivers(@RequestBody PubCarDriverDto pubCarDriverDto) {

        //校验是否传了 分页信息

        int page = pubCarDriverDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarDriverDto.setPage((page - 1) * pubCarDriverDto.getRow());
        }

        return pubCarDriverServiceDaoImpl.queryPubCarDrivers(pubCarDriverDto);
    }


    @Override
    public int queryPubCarDriversCount(@RequestBody PubCarDriverDto pubCarDriverDto) {
        return pubCarDriverServiceDaoImpl.queryPubCarDriversCount(BeanConvertUtil.beanCovertMap(pubCarDriverDto));    }

    @Override
    public String queryPubCarDriversByValidateCondition(String jobNum) {
        return pubCarDriverServiceDaoImpl.queryPubCarDriversByValidateCondition(jobNum);
    }

    @Override
    public List<ApiPubCarSimpleDriverDataVo> queryPubCarDriverNames() {
        return pubCarDriverServiceDaoImpl.queryPubCarDriverNames();
    }

    @Override
    public boolean existPubCarDriver(@RequestBody PubCarExistDriverDto existDriverDto) {
        return Objects.nonNull(pubCarDriverServiceDaoImpl.existPubCarDriver(existDriverDto));
    }
}

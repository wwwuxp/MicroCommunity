package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarRepairServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarRepair.PubCarRepairDto;
import com.java110.intf.pubcar.IPubCarRepairInnerServiceSMO;
import com.java110.po.pubCarRepair.PubCarRepairPo;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 车辆维修信息表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarRepairInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarRepairInnerServiceSMO {

    @Autowired
    private IPubCarRepairServiceDao pubCarRepairServiceDaoImpl;


    @Override
    public int savePubCarRepair(@RequestBody PubCarRepairPo pubCarRepairPo) {
        int saveFlag = 1;
        pubCarRepairServiceDaoImpl.savePubCarRepairInfo(BeanConvertUtil.beanCovertMap(pubCarRepairPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarRepair(@RequestBody PubCarRepairPo pubCarRepairPo) {
        int saveFlag = 1;
        pubCarRepairServiceDaoImpl.updatePubCarRepairInfo(BeanConvertUtil.beanCovertMap(pubCarRepairPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarRepair(@RequestBody PubCarRepairPo pubCarRepairPo) {
        int saveFlag = 1;
        pubCarRepairPo.setStatusCd("1");
        pubCarRepairServiceDaoImpl.updatePubCarRepairInfo(BeanConvertUtil.beanCovertMap(pubCarRepairPo));
        return saveFlag;
    }

    @Override
    public List<PubCarRepairDto> queryPubCarRepairs(@RequestBody PubCarRepairDto pubCarRepairDto) {

        //校验是否传了 分页信息

        int page = pubCarRepairDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarRepairDto.setPage((page - 1) * pubCarRepairDto.getRow());
        }

        List<PubCarRepairDto> pubCarRepairs = BeanConvertUtil.covertBeanList(pubCarRepairServiceDaoImpl.getPubCarRepairInfo(BeanConvertUtil.beanCovertMap(pubCarRepairDto)), PubCarRepairDto.class);

        return pubCarRepairs;
    }


    @Override
    public int queryPubCarRepairsCount(@RequestBody PubCarRepairDto pubCarRepairDto) {
        return pubCarRepairServiceDaoImpl.queryPubCarRepairsCount(BeanConvertUtil.beanCovertMap(pubCarRepairDto));
    }

}

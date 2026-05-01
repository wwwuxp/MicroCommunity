package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarTripCarNumServiceDao;
import com.hezong.pubcar.dao.IPubCarTripMileageServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.po.pubCarTripMileage.PubCarTripMileagePo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarTripMileage.ApiTripMileageAndFeeDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 行驶里程信息表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarTripMileageInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarTripMileageInnerServiceSMO {

    @Autowired
    private IPubCarTripMileageServiceDao pubCarTripMileageServiceDaoImpl;

    @Autowired
    private IPubCarTripCarNumServiceDao pubCarTripCarNumServiceDaoImpl;


    @Override
    public int savePubCarTripMileage(@RequestBody PubCarTripMileagePo pubCarTripMileagePo) {
        int saveFlag = 1;
        pubCarTripMileageServiceDaoImpl.savePubCarTripMileageInfo(BeanConvertUtil.beanCovertMap(pubCarTripMileagePo));
        return saveFlag;
    }

    @Override
    public int updatePubCarTripMileage(@RequestBody PubCarTripMileagePo pubCarTripMileagePo) {
        int saveFlag = 1;
        pubCarTripMileageServiceDaoImpl.updatePubCarTripMileageInfo(BeanConvertUtil.beanCovertMap(pubCarTripMileagePo));
        return saveFlag;
    }

    @Override
    public int deletePubCarTripMileage(@RequestBody PubCarTripMileagePo pubCarTripMileagePo) {
        int saveFlag = 1;
        pubCarTripMileagePo.setStatusCd("1");
        pubCarTripMileageServiceDaoImpl.updatePubCarTripMileageInfo(BeanConvertUtil.beanCovertMap(pubCarTripMileagePo));
        return saveFlag;
    }

    @Override
    public List<PubCarTripMileageDto> queryPubCarTripMileages(@RequestBody PubCarTripMileageDto pubCarTripMileageDto) {

        //校验是否传了 分页信息

        int page = pubCarTripMileageDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarTripMileageDto.setPage((page - 1) * pubCarTripMileageDto.getRow());
        }

        return BeanConvertUtil.covertBeanList(pubCarTripMileageServiceDaoImpl.getPubCarTripMileageInfo(BeanConvertUtil.beanCovertMap(pubCarTripMileageDto)), PubCarTripMileageDto.class);
    }


    @Override
    public int queryPubCarTripMileagesCount(@RequestBody PubCarTripMileageDto pubCarTripMileageDto) {
        return pubCarTripMileageServiceDaoImpl.queryPubCarTripMileagesCount(BeanConvertUtil.beanCovertMap(pubCarTripMileageDto));
    }


    @Override
    public List<PubCarTripMileageDto> queryAllotCarTrip(String tripId, String communityId) {
        PubCarTripMileageDto pubCarTripMileageDto = new PubCarTripMileageDto();
        pubCarTripMileageDto.setTripId(tripId);
        pubCarTripMileageDto.setCommunityId(communityId);
        return pubCarTripMileageServiceDaoImpl.queryDriverTrip(pubCarTripMileageDto);
    }

    @Override
    public boolean getDriverTripIsInProgress(@RequestBody PubCarTripMileageDto pubCarTripMileageDto) {
        int count = pubCarTripMileageServiceDaoImpl.queryDriverTripIsInProgress(pubCarTripMileageDto);
        // 在进行中
        return count > 0;
    }

    @Override
    public Double queryEndMileageOfPreviousCar(String carId, String communityId) {
        Map<String, String> map = new HashMap<>();
        // map.put("tripId", tripId);
        // map.put("driverId", driverId);
        map.put("communityId", communityId);

        // 根据司机id和行程id获取车辆id
        // String carId = pubCarTripCarNumServiceDaoImpl.getCarIdByTripIdAndDriverId(map);
        // if (StringUtils.isBlank(carId)){
        //     return null;
        // }
        map.put("carId",carId);
        return pubCarTripMileageServiceDaoImpl.queryEndMileageOfPreviousCar(map);
    }

    @Override
    public ApiTripMileageAndFeeDataVo getTripMileageAndFee(@RequestBody PubCarTripMileageDto pubCarTripMileageDto) {
        return pubCarTripMileageServiceDaoImpl.getTripMileageAndFee(pubCarTripMileageDto);
    }

    @Override
    public int updateTripMileageAndFee(@RequestBody PubCarTripMileageDto pubCarTripMileageDto) {
        return pubCarTripMileageServiceDaoImpl.updateTripMileageAndFee(pubCarTripMileageDto);
    }
}

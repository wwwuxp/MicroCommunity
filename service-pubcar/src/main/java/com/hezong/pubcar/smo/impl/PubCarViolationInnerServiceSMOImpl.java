package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarUserServiceDao;
import com.hezong.pubcar.dao.IPubCarViolationServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarViolation.PubCarViolationDto;
import com.java110.dto.user.UserSimpleDto;
import com.java110.intf.pubcar.IPubCarViolationInnerServiceSMO;
import com.java110.po.pubCarViolation.PubCarViolationPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarViolation.ApiPubCarViolationDataVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 车辆违章信息表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarViolationInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarViolationInnerServiceSMO {

    @Autowired
    private IPubCarViolationServiceDao pubCarViolationServiceDaoImpl;

    @Autowired
    private IPubCarUserServiceDao pubCarUserServiceDaoImpl;


    @Override
    public int savePubCarViolation(@RequestBody PubCarViolationPo pubCarViolationPo) {
        int saveFlag = 1;
        pubCarViolationServiceDaoImpl.savePubCarViolationInfo(BeanConvertUtil.beanCovertMap(pubCarViolationPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarViolation(@RequestBody PubCarViolationPo pubCarViolationPo) {
        int saveFlag = 1;
        pubCarViolationServiceDaoImpl.updatePubCarViolationInfo(BeanConvertUtil.beanCovertMap(pubCarViolationPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarViolation(@RequestBody PubCarViolationPo pubCarViolationPo) {
        int saveFlag = 1;
        pubCarViolationPo.setStatusCd("1");
        pubCarViolationServiceDaoImpl.updatePubCarViolationInfo(BeanConvertUtil.beanCovertMap(pubCarViolationPo));
        return saveFlag;
    }

    @Override
    public List<ApiPubCarViolationDataVo> queryPubCarViolations(@RequestBody PubCarViolationDto pubCarViolationDto) {
        //校验是否传了 分页信息
        int page = pubCarViolationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarViolationDto.setPage((page - 1) * pubCarViolationDto.getRow());
        }

        if (Objects.nonNull(pubCarViolationDto.getDriverId())){
            return pubCarViolationServiceDaoImpl.listPubCarDriverViolations(pubCarViolationDto);
        }

        List<ApiPubCarViolationDataVo> violationDataVos = pubCarViolationServiceDaoImpl.listPubCarViolations(pubCarViolationDto);
        // 是自驾类型的司机id(也即用车人id)
        List<String> userIds = violationDataVos.stream()
                .filter(t -> Objects.isNull(t.getDriverName()) && Objects.isNull(t.getDepartment()) && Objects.isNull(t.getPosition()))
                .map(ApiPubCarViolationDataVo::getDriverId)
                .distinct()
                .collect(Collectors.toList());
        // 查询对应的姓名，部门，职位
        if (CollectionUtils.isNotEmpty(userIds)){
            // 如果注入IUserInnerServiceSMO，查询操作在该类下，会出现数据库分片错误
            List<UserSimpleDto> userSimpleDtos = pubCarUserServiceDaoImpl.getNameDepartmentPositionInfoBatch(userIds);
            Map<String, UserSimpleDto> userSimpleDtoMap = userSimpleDtos.stream()
                    .collect(Collectors.toMap(UserSimpleDto::getUserId, a -> a));
            // 根据id对应信息
            for (ApiPubCarViolationDataVo violationDataVo : violationDataVos) {
                String driverId = violationDataVo.getDriverId();
                if (userSimpleDtoMap.containsKey(driverId)){
                    UserSimpleDto userSimpleDto = userSimpleDtoMap.get(driverId);
                    violationDataVo.setDriverName(userSimpleDto.getName());
                    violationDataVo.setDepartment(userSimpleDto.getDepartment());
                    violationDataVo.setPosition(userSimpleDto.getPosition());
                }
                violationDataVo.setDriverId(null);
            }
        }

        return violationDataVos;
    }


    @Override
    public int queryPubCarViolationsCount(@RequestBody PubCarViolationDto pubCarViolationDto) {
        return pubCarViolationServiceDaoImpl.queryPubCarViolationsCount(pubCarViolationDto);
    }

}

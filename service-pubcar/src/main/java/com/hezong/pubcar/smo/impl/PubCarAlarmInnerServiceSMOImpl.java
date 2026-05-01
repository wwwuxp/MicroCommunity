package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarAlarmServiceDao;
import com.hezong.pubcar.dao.IPubCarUserServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import com.java110.intf.pubcar.IPubCarAlarmInnerServiceSMO;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.pubCarAlarm.ApiPubCarAlarmDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 异常报警信息表内部服务实现类
 * add by hh
 **/
@RestController
public class PubCarAlarmInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarAlarmInnerServiceSMO {

    @Autowired
    private IPubCarAlarmServiceDao pubCarAlarmServiceDaoImpl;

    @Autowired
    private IPubCarUserServiceDao pubCarUserServiceDaoImpl;

    @Override
    public int savePubCarAlarm(@RequestBody PubCarAlarmPo pubCarAlarmPo) {
        int saveFlag = 1;
        pubCarAlarmServiceDaoImpl.savePubCarAlarmInfo(BeanConvertUtil.beanCovertMap(pubCarAlarmPo));
        return saveFlag;
    }

    @Override
    public int updatePubCarAlarm(@RequestBody PubCarAlarmPo pubCarAlarmPo) {
        int saveFlag = 1;
        pubCarAlarmServiceDaoImpl.updatePubCarAlarmInfo(BeanConvertUtil.beanCovertMap(pubCarAlarmPo));
        return saveFlag;
    }

    @Override
    public int deletePubCarAlarm(@RequestBody PubCarAlarmPo pubCarAlarmPo) {
        int saveFlag = 1;
        pubCarAlarmPo.setStatusCd("1");
        pubCarAlarmServiceDaoImpl.updatePubCarAlarmInfo(BeanConvertUtil.beanCovertMap(pubCarAlarmPo));
        return saveFlag;
    }

    @Override
    public List<ApiPubCarAlarmDataVo> queryPubCarAlarms(@RequestBody PubCarAlarmDto pubCarAlarmDto) {
        //校验是否传了 分页信息
        int page = pubCarAlarmDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarAlarmDto.setPage((page - 1) * pubCarAlarmDto.getRow());
        }

        // 查询除异常数据列表
        List<ApiPubCarAlarmDataVo> pubCarAlarmInfos = pubCarAlarmServiceDaoImpl.getPubCarAlarmInfo(pubCarAlarmDto);
        for (ApiPubCarAlarmDataVo pubCarAlarmInfo : pubCarAlarmInfos) {
            String grade = pubCarAlarmInfo.getGrade();
            if (PubCarAlarmPo.ALARM_LEVEL_HIGH.equals(grade)){
                pubCarAlarmInfo.setGrade("高");
            }else if (PubCarAlarmPo.ALARM_LEVEL_MIDDLE.equals(grade)){
                pubCarAlarmInfo.setGrade("中");
            }
        }

        /*// 是自驾类型的司机id(也即用车人id)
        List<String> userIds = pubCarAlarmInfos.stream()
                .filter(t -> Objects.isNull(t.getDriver()) && Objects.isNull(t.getDepartment()) && Objects.isNull(t.getPosition()))
                .map(ApiPubCarAlarmDataVo::getDriverId)
                .distinct()
                .collect(Collectors.toList());
        // 查询对应的姓名，部门，职位
        if (CollectionUtils.isNotEmpty(userIds)){
            // 如果注入IUserInnerServiceSMO，查询操作在该类下，会出现数据库分片错误
            List<UserSimpleDto> userSimpleDtos = pubCarUserServiceDaoImpl.getNameDepartmentPositionInfoBatch(userIds);
            Map<String, UserSimpleDto> userSimpleDtoMap = userSimpleDtos.stream()
                    .collect(Collectors.toMap(UserSimpleDto::getUserId, a -> a));
            // 根据id对应信息
            for (ApiPubCarAlarmDataVo pubCarAlarmDataVo : pubCarAlarmInfos) {
                String driverId = pubCarAlarmDataVo.getDriverId();
                if (userSimpleDtoMap.containsKey(driverId)){
                    UserSimpleDto userSimpleDto = userSimpleDtoMap.get(driverId);
                    pubCarAlarmDataVo.setDriver(userSimpleDto.getName());
                    pubCarAlarmDataVo.setDepartment(userSimpleDto.getDepartment());
                    pubCarAlarmDataVo.setPosition(userSimpleDto.getPosition());
                }
                pubCarAlarmDataVo.setDriverId(null);
            }
        }*/

        return pubCarAlarmInfos;
    }


    @Override
    public int queryPubCarAlarmsCount(@RequestBody PubCarAlarmDto pubCarAlarmDto) {
        return pubCarAlarmServiceDaoImpl.queryPubCarAlarmsCount(pubCarAlarmDto);
    }

    @Override
    public void savePubCarAlarmBatch(@RequestBody List<PubCarAlarmPo> pubCarAlarmPos) {
        pubCarAlarmServiceDaoImpl.savePubCarAlarmBatch(pubCarAlarmPos);
    }
}

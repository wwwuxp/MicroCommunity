package com.java110.front.smo.pubCarAlarm.impl;

import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.keyCabinetLog.KeyCabinetLogDto;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.intf.pubcar.IPubCarAlarmInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.intf.pubcar.IkeyCabinetLogInnerServiceSMO;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.po.pubCarTrip.PubCarTripPo;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ScanResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成公车报警数据
 * 注意：还有一种生成报警不在此工程中，在hc系统的辅助工程中
 * @author hh
 * @date 2021/10/20
 */
@Component
public class GeneratePubCarAlarmTask {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    @Autowired
    private IPubCarAlarmInnerServiceSMO pubCarAlarmInnerServiceSMOImpl;

    @Autowired
    private IkeyCabinetLogInnerServiceSMO KeyCabinetLogInnerServiceSMOImpl;

    // @Scheduled(cron = "0 0/10 * * * ?")
    protected void process() {
        List<String> tripIdKeys = new ArrayList<>();
        String cursor = "0";
        do {
            ScanResult<String> scanResult = CommonCache.scan(cursor, CommonConstant.APPLY_PUBCAR + "*", 300);
            List<String> result = scanResult.getResult();
            // 如果redis中没有数据，不扫描mysql
            if (CollectionUtils.isEmpty(result)){
                return;
            }

            tripIdKeys.addAll(result);
            cursor = scanResult.getStringCursor();
        }while (!"0".equals(cursor));

        if (CollectionUtils.isEmpty(tripIdKeys)){
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        // 管理员在行程开始1小时内未进行审核
        List<String> beforeStart1HourTripIdList = new ArrayList<>();
        // 钥匙未归还。离用车申请结束时间超过24小时，钥匙仍未归还
        List<String> afterEnd24HourTripIdList = new ArrayList<>();
        // 钥匙超时归还。车钥匙已经归还到钥匙柜，但是归还时间超过用车申请结束时间12个小时以上
        List<String> afterEnd12HourTripIdList = new ArrayList<>();
        // 超时领取钥匙。车钥匙领取时间比申请时间晚2小时以上
        List<String> afterStart2HourTripIdList = new ArrayList<>();

        for (String tripIdKey : tripIdKeys) {
            // 此value应该是申请用车开始时间或申请用车结束时间
            String value = CommonCache.getValue(tripIdKey);
            LocalDateTime time = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));

            // key前缀
            String tripIdKeyPrefix = tripIdKey.substring(0, tripIdKey.lastIndexOf("_") + 1);
            switch (tripIdKeyPrefix){
                case CommonConstant.APPLY_PUBCAR_START:
                    long between = ChronoUnit.HOURS.between(now, time);
                    if (between == 0){
                        beforeStart1HourTripIdList.add(tripIdKey.replace(CommonConstant.APPLY_PUBCAR_START, ""));
                    }
                    break;
                case CommonConstant.APPLY_PUBCAR_END24:
                    long between1 = ChronoUnit.HOURS.between(time, now);
                    // 距离申请用车结束时间超过24小时
                    if (between1 >= 24){
                        afterEnd24HourTripIdList.add(tripIdKey.replace(CommonConstant.APPLY_PUBCAR_END24, ""));
                    }
                    break;
                case CommonConstant.APPLY_PUBCAR_END12:
                    afterEnd12HourTripIdList.add(tripIdKey.replace(CommonConstant.APPLY_PUBCAR_END12, ""));
                    break;
                case CommonConstant.APPLY_PUBCAR_START2:
                    afterStart2HourTripIdList.add(tripIdKey.replace(CommonConstant.APPLY_PUBCAR_START2, ""));
                    break;
                default:
            }
        }

        // 汇入一个集合
        Set<String> allTripIdSet = new HashSet<>();
        allTripIdSet.addAll(beforeStart1HourTripIdList);
        allTripIdSet.addAll(afterEnd24HourTripIdList);
        allTripIdSet.addAll(afterEnd12HourTripIdList);
        allTripIdSet.addAll(afterStart2HourTripIdList);

        if (CollectionUtils.isEmpty(allTripIdSet)){
            return;
        }

        // 根据行程id集合查询行程信息
        List<PubCarTripDto> pubCarTripDtos = pubCarTripInnerServiceSMOImpl.queryPubCarTripsByTripIdsBatch(new ArrayList<>(allTripIdSet));
        // 车钥匙集合
        List<String> keyCabinetList = pubCarTripDtos.stream()
                .map(PubCarTripDto::getKeyCabinet)
                .filter(Objects::nonNull).collect(Collectors.toList());
        // 只是查询到钥匙柜最新的(最近一条)取还钥匙日志记录,用于判断是取还是还
        List<KeyCabinetLogDto> keyCabinetLogDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(keyCabinetList)){
            keyCabinetLogDtoList = KeyCabinetLogInnerServiceSMOImpl.getKeyCabinetLogByKeyCabinetsBatch(keyCabinetList);
        }
        // list转map
        Map<String, Integer> keyNoAndTypeMap = keyCabinetLogDtoList.stream().collect(Collectors.toMap(KeyCabinetLogDto::getKeyNo, KeyCabinetLogDto::getType, (k1, k2) -> k2));
        // list转map
        Map<String, PubCarTripDto> tripIdAndValueMap = pubCarTripDtos.stream().collect(Collectors.toMap(PubCarTripDto::getTripId, t -> t, (k1, k2) -> k2));

        List<PubCarAlarmPo> pubCarAlarmPos = new ArrayList<>();
        // 已经存入库中的用车报警行程id
        List<String> hasToDbKeys = new ArrayList<>();

        // 处理距用车1小时未审批
        handleBeforeStart1HourData(now, beforeStart1HourTripIdList, tripIdAndValueMap, pubCarAlarmPos, hasToDbKeys);
        // 处理钥匙未归还
        handleAfterEnd24HourData(afterEnd24HourTripIdList, keyNoAndTypeMap, tripIdAndValueMap, pubCarAlarmPos, hasToDbKeys);
        // 处理钥匙超时归还
        handleAfterEnd12HourData(afterEnd12HourTripIdList, tripIdAndValueMap, pubCarAlarmPos, hasToDbKeys);
        // 处理超时领取钥匙
        handleAfterStart2HourData(afterStart2HourTripIdList, tripIdAndValueMap, pubCarAlarmPos, hasToDbKeys);

        if (CollectionUtils.isEmpty(pubCarAlarmPos)){
            return;
        }

        // 批量保存用车报警信息
        pubCarAlarmInnerServiceSMOImpl.savePubCarAlarmBatch(pubCarAlarmPos);

        // 添加完后将key从redis中删除，避免再次被扫描到
        for (String hasToDbTripIdKey : hasToDbKeys) {
            CommonCache.removeValue(hasToDbTripIdKey);
        }

    }

    /**
     * 超时领取钥匙（中）。车钥匙领取时间比申请时间晚2小时以上。
     * @param afterStart2HourTripIdList afterStart2HourTripIdList
     * @param tripIdAndValueMap tripIdAndValueMap
     * @param pubCarAlarmPos pubCarAlarmPos
     * @param hasToDbKeys hasToDbKeys
     */
    private void handleAfterStart2HourData(List<String> afterStart2HourTripIdList, Map<String, PubCarTripDto> tripIdAndValueMap, List<PubCarAlarmPo> pubCarAlarmPos, List<String> hasToDbKeys) {
        for (String tripId : afterStart2HourTripIdList) {
            PubCarTripDto pubCarTripDto = tripIdAndValueMap.get(tripId);
            if (Objects.isNull(pubCarTripDto)){
                continue;
            }
            String keyCabinet = pubCarTripDto.getKeyCabinet();
            String applicant = pubCarTripDto.getApplicant();
            String department = pubCarTripDto.getDepartment();

            // 要根据姓名，部门和对应的钥匙来获取记录时间和类型
            KeyCabinetLogDto keyCabinetLogDto = new KeyCabinetLogDto();
            keyCabinetLogDto.setKeyNo(keyCabinet);
            keyCabinetLogDto.setName(applicant);
            keyCabinetLogDto.setDepartment(department);
            KeyCabinetLogDto responseKeyCabinetDto = KeyCabinetLogInnerServiceSMOImpl.getKeyCabinetLogByNameAndDepartmentAndKey(keyCabinetLogDto);
            if (Objects.isNull(responseKeyCabinetDto)){
                continue;
            }
            // 类型（1：取钥匙，2：还钥匙）
            Integer type = responseKeyCabinetDto.getType();
            if (Objects.equals(type, 1)){
                // 钥匙领取时间
                String pickUpTimeStr = responseKeyCabinetDto.getRecordTime();
                // 申请开始用车时间
                String startTimeStr = pubCarTripDto.getStartTime();
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                LocalDateTime pickUpTime = LocalDateTime.parse(pickUpTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                long between = ChronoUnit.HOURS.between(startTime, pickUpTime);
                if (between >= 2){
                    PubCarAlarmPo pubCarAlarmPo = new PubCarAlarmPo();
                    pubCarAlarmPo.setAlarmId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ALARM_ID));
                    pubCarAlarmPo.setName(PubCarAlarmPo.OVERTIME_KEY_COLLECTION);
                    pubCarAlarmPo.setGrade(PubCarAlarmPo.ALARM_LEVEL_MIDDLE);
                    pubCarAlarmPo.setTripId(pubCarTripDto.getTripId());
                    pubCarAlarmPo.setDetail("车钥匙领取时间比申请时间晚2小时以上。");
                    pubCarAlarmPo.setCommunityId(pubCarTripDto.getCommunityId());
                    pubCarAlarmPo.setbId("-1");
                    pubCarAlarmPos.add(pubCarAlarmPo);
                    hasToDbKeys.add(CommonConstant.APPLY_PUBCAR_START2 + pubCarTripDto.getTripId());
                }
            }
        }
    }

    /**
     * 钥匙超时归还（高）。车钥匙已经归还到钥匙柜，但是归还时间超过用车申请结束时间12个小时以上。
     * @param afterEnd12HourTripIdList afterEnd12HourTripIdList
     * @param tripIdAndValueMap tripIdAndValueMap
     * @param pubCarAlarmPos pubCarAlarmPos
     * @param hasToDbKeys hasToDbKeys
     */
    private void handleAfterEnd12HourData(List<String> afterEnd12HourTripIdList, Map<String, PubCarTripDto> tripIdAndValueMap, List<PubCarAlarmPo> pubCarAlarmPos, List<String> hasToDbKeys) {
        for (String tripId : afterEnd12HourTripIdList) {
            PubCarTripDto pubCarTripDto = tripIdAndValueMap.get(tripId);
            if (Objects.isNull(pubCarTripDto)){
                continue;
            }

            Integer approvalStatus = pubCarTripDto.getApprovalStatus();
            if (!Objects.equals(approvalStatus, PubCarTripPo.APPROVAL_STATUS_PASS)){
                continue;
            }

            // 要根据姓名，部门和对应的钥匙来获取记录时间和类型
            KeyCabinetLogDto keyCabinetLogDto = new KeyCabinetLogDto();
            keyCabinetLogDto.setKeyNo(pubCarTripDto.getKeyCabinet());
            keyCabinetLogDto.setName(pubCarTripDto.getApplicant());
            keyCabinetLogDto.setDepartment(pubCarTripDto.getDepartment());
            KeyCabinetLogDto responseKeyCabinetDto = KeyCabinetLogInnerServiceSMOImpl.getKeyCabinetLogByNameAndDepartmentAndKey(keyCabinetLogDto);
            if (Objects.isNull(responseKeyCabinetDto)){
                continue;
            }
            // 类型（1：取钥匙，2：还钥匙）
            Integer type = responseKeyCabinetDto.getType();
            if (Objects.equals(type, 2)){
                // 钥匙已经还到钥匙柜，归还时间超过申请结束时间12个小时以上，加入异常报警
                String returnTimeStr = responseKeyCabinetDto.getRecordTime();
                String endTimeStr = pubCarTripDto.getEndTime();
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                LocalDateTime returnTime = LocalDateTime.parse(returnTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                long between = ChronoUnit.HOURS.between(endTime, returnTime);
                if (between >= 12){
                    PubCarAlarmPo pubCarAlarmPo = new PubCarAlarmPo();
                    pubCarAlarmPo.setAlarmId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ALARM_ID));
                    pubCarAlarmPo.setName(PubCarAlarmPo.KEY_RETURNED_OVER_TIME);
                    pubCarAlarmPo.setGrade(PubCarAlarmPo.ALARM_LEVEL_HIGH);
                    pubCarAlarmPo.setTripId(pubCarTripDto.getTripId());
                    pubCarAlarmPo.setDetail("车钥匙已经归还到钥匙柜，但是归还时间超过用车申请结束时间12个小时以上。");
                    pubCarAlarmPo.setCommunityId(pubCarTripDto.getCommunityId());
                    pubCarAlarmPo.setbId("-1");
                    pubCarAlarmPos.add(pubCarAlarmPo);
                    hasToDbKeys.add(CommonConstant.APPLY_PUBCAR_END12 + pubCarTripDto.getTripId());
                }
            }
        }
    }

    /**
     * 钥匙未归还（高）。离用车申请结束时间超过24小时，钥匙仍未归还。
     * @param afterEnd24HourTripIdList afterEnd24HourTripIdList
     * @param keyNoAndTypeMap keyNoAndTypeMap
     * @param tripIdAndValueMap tripIdAndValueMap
     * @param pubCarAlarmPos pubCarAlarmPos
     * @param hasToDbKeys hasToDbKeys
     */
    private void handleAfterEnd24HourData(List<String> afterEnd24HourTripIdList, Map<String, Integer> keyNoAndTypeMap, Map<String, PubCarTripDto> tripIdAndValueMap, List<PubCarAlarmPo> pubCarAlarmPos, List<String> hasToDbKeys) {
        for (String tripId : afterEnd24HourTripIdList) {
            PubCarTripDto pubCarTripDto = tripIdAndValueMap.get(tripId);
            if (Objects.isNull(pubCarTripDto)){
                continue;
            }
            String keyCabinet = pubCarTripDto.getKeyCabinet();
            // 类型（1：取钥匙，2：还钥匙）
            Integer keyType = keyNoAndTypeMap.get(keyCabinet);
            if (Objects.equals(keyType, 1)){
                // 生成报警信息
                PubCarAlarmPo pubCarAlarmPo = new PubCarAlarmPo();
                pubCarAlarmPo.setAlarmId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ALARM_ID));
                pubCarAlarmPo.setName(PubCarAlarmPo.KEY_NOT_RETURNED);
                pubCarAlarmPo.setGrade(PubCarAlarmPo.ALARM_LEVEL_HIGH);
                pubCarAlarmPo.setTripId(pubCarTripDto.getTripId());
                pubCarAlarmPo.setDetail("离用车申请结束时间超过24小时，钥匙仍未归还。");
                pubCarAlarmPo.setCommunityId(pubCarTripDto.getCommunityId());
                pubCarAlarmPo.setbId("-1");
                pubCarAlarmPos.add(pubCarAlarmPo);
                hasToDbKeys.add(CommonConstant.APPLY_PUBCAR_END24 + pubCarTripDto.getTripId());
            }
        }
    }

    /**
     * 处理距离申请开始时间还有1小时的数据
     * @param now 当前时间
     * @param beforeStart1HourList 距离申请开始时间还有1小时的数据集合
     * @param tripIdAndValueMap 行程id和PubCarTripDto的map
     * @param pubCarAlarmPos 将要入库的用车报警信息集合
     * @param hasToDbKeys 已经入库的行程id
     */
    private void handleBeforeStart1HourData(LocalDateTime now, List<String> beforeStart1HourList, Map<String, PubCarTripDto> tripIdAndValueMap, List<PubCarAlarmPo> pubCarAlarmPos, List<String> hasToDbKeys) {
        for (String tripId : beforeStart1HourList) {
            PubCarTripDto pubCarTripDto = tripIdAndValueMap.get(tripId);
            if (Objects.isNull(pubCarTripDto)){
                continue;
            }

            Integer approvalStatus = pubCarTripDto.getApprovalStatus();

            // 仍未审核
            if (Objects.equals(approvalStatus, PubCarTripPo.APPROVAL_STATUS_UNREVIEWED)){
                // 创建时间即提交申请的时间
                String applyTimeStr = pubCarTripDto.getCreateTime();
                LocalDateTime applyTime = LocalDateTime.parse(applyTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_A));
                long between = ChronoUnit.HOURS.between(applyTime, now);
                // 用车人提交申请已经超过2个小时
                if (between >= 2){
                    PubCarAlarmPo pubCarAlarmPo = new PubCarAlarmPo();
                    pubCarAlarmPo.setAlarmId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_ALARM_ID));
                    pubCarAlarmPo.setName(PubCarAlarmPo.NOT_APPROVE);
                    pubCarAlarmPo.setGrade(PubCarAlarmPo.ALARM_LEVEL_HIGH);
                    pubCarAlarmPo.setTripId(pubCarTripDto.getTripId());
                    pubCarAlarmPo.setDetail("原定开始时间：" +  pubCarTripDto.getStartTime() + "，距离开始时间不足1小时，未审核且提交申请已经超过2小时");
                    pubCarAlarmPo.setCommunityId(pubCarTripDto.getCommunityId());
                    pubCarAlarmPo.setbId("-1");
                    pubCarAlarmPos.add(pubCarAlarmPo);
                    hasToDbKeys.add(CommonConstant.APPLY_PUBCAR_START + pubCarTripDto.getTripId());
                }
            }
        }
    }

}

package com.java110.api.listener.pubCarTripMileage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.bmo.pubCarAlarm.IPubCarAlarmBMO;
import com.java110.api.bmo.pubCarTrip.IPubCarTripBMO;
import com.java110.api.bmo.pubCarTripMileage.IPubCarTripMileageBMO;
import com.java110.api.listener.AbstractServiceApiPlusListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.dto.pubCarTripMileage.PubCarTripMileageDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarTripMileageInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.file.FileRelPo;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceCodePubCarTripMileageConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ConstantUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 添加当前里程侦听(司机开始行程或结束行程)
 * add by hh
 */
@Java110Listener("savePubCarTripMileageListener")
public class SavePubCarTripMileageListener extends AbstractServiceApiPlusListener {

    @Autowired
    private IPubCarTripMileageBMO pubCarTripMileageBMOImpl;
    @Autowired
    private IPubCarTripMileageInnerServiceSMO pubCarTripMileageInnerServiceSMOImpl;
    @Autowired
    private IPubCarTripBMO pubCarTripBMOImpl;
    @Autowired
    private IPubCarAlarmBMO pubCarAlarmBMOImpl;
    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;
    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区id不能为空");
        Assert.hasKeyAndValue(reqJson, "tripId", "行程id不能为空");
        Assert.hasKeyAndValue(reqJson, "driverId", "司机id不能为空");
        Assert.hasKeyAndValue(reqJson, "carId", "当前行程还未由用车管理员审批");
        // Assert.hasKeyAndValue(reqJson, "currentMileage", "请填写当前里程数");
        // Assert.hasKeyAndValue(reqJson, "currentTime", "当前时间不能为空");
        Assert.hasKeyAndValue(reqJson, "startMileage", "请填写开始里程数");
        Assert.hasKeyAndValue(reqJson, "startTime", "开始时间不能为空");
        Assert.hasKeyAndValue(reqJson, "endMileage", "请填写结束里程数");
        Assert.hasKeyAndValue(reqJson, "endTime", "结束时间不能为空");

        if (StringUtils.isBlank(reqJson.getString("repairFee"))){
            reqJson.put("repairFee", "0");
        }
        if (StringUtils.isBlank(reqJson.getString("oilFee"))){
            reqJson.put("oilFee", "0");
        }
        if (StringUtils.isBlank(reqJson.getString("parkingFee"))){
            reqJson.put("parkingFee", "0");
        }
        if (StringUtils.isBlank(reqJson.getString("roadBridgeFee"))){
            reqJson.put("roadBridgeFee", "0");
        }

        // Object currentMileage = reqJson.get("currentMileage");
        Object startMileage = reqJson.get("startMileage");
        Object endMileage = reqJson.get("endMileage");
        if (startMileage instanceof String) {
            String startMileageString = (String) startMileage;
            if (!startMileageString.matches(ConstantUtil.REGEX_DECIMAL)) {
                throw new IllegalArgumentException("开始里程数（默认公里）只能含有小数点或数字");
            }
            reqJson.put("startMileage", Double.parseDouble(startMileageString));
        }else if(endMileage instanceof String){
            String endMileageString = (String) endMileage;
            if (!endMileageString.matches(ConstantUtil.REGEX_DECIMAL)) {
                throw new IllegalArgumentException("结束里程数（默认公里）只能含有小数点或数字");
            }
            reqJson.put("endMileage", Double.parseDouble(endMileageString));
        }

        if (reqJson.getDouble("startMileage") > reqJson.getDouble("endMileage")){
            throw new IllegalArgumentException("开始里程不能大于结束里程");
        }

        // if (reqJson.getLong("startTime") > reqJson.getLong("endTime")){
        //     throw new IllegalArgumentException("开始行程时间不能大于结束行程时间");
        // }
    }

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) throws ParseException {
        //原版保存里程信息
        // doSavePubCarTripMileage(context,reqJson);
        //合并开始行程结束行程保存里程信息
        doSavePubCarTripMileageV2(context,reqJson);
    }

    private void doSavePubCarTripMileage(DataFlowContext context, JSONObject reqJson){
        // 查询数据库中是否有了开始里程，若有，则把数据更新到结束里程上
        String tripId = reqJson.getString("tripId");
        String driverId = reqJson.getString("driverId");
        String carId = reqJson.getString("carId");
        String communityId = reqJson.getString("communityId");
        Long currentTime = reqJson.getLong("currentTime");
        List<PubCarTripMileageDto> carTripMileages = pubCarTripMileageInnerServiceSMOImpl.queryAllotCarTrip(tripId, communityId);

        List<PubCarTripMileageDto> currentDriverMileages = carTripMileages.stream().filter(t -> driverId.equals(t.getDriverId())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(currentDriverMileages)) {
            reqJson.put("mileageId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_MILEAGE));
            // 保存图片
            // saveImages(context, reqJson);
            // 添加里程(开始里程)
            pubCarTripMileageBMOImpl.addTripMileage(reqJson, context);
            // 标记行程状态为进行中
            pubCarTripBMOImpl.updateTripStatus(reqJson, context, StatusConstant.STATUS_RUNNING);

            // 查询上一部车的结束里程
            Double endMileage = pubCarTripMileageInnerServiceSMOImpl.queryEndMileageOfPreviousCar(carId, communityId);
            if (Objects.nonNull(endMileage)){
                Double currentMileage = reqJson.getDouble("currentMileage");
                if (!endMileage.equals(currentMileage)){
                    BigDecimal currentDecimal = new BigDecimal(currentMileage.toString());
                    BigDecimal endDecimal = new BigDecimal(endMileage.toString());
                    // 误差里程
                    double errorMileage = currentDecimal.subtract(endDecimal).doubleValue();
                    if (errorMileage > 10){
                        String driverName = userInnerServiceSMOImpl.getUserNameByUserId(driverId);
                        String detail = "上个司机提交的结束里程为：" + endMileage + "公里，当前司机提交的开始里程为：" + currentMileage;
                        PubCarTripMileageDto pubCarTripMileageDto = new PubCarTripMileageDto();
                        pubCarTripMileageDto.setDriverId(driverId);
                        pubCarTripMileageDto.setTripId(tripId);
                        pubCarTripMileageDto.setCommunityId(communityId);
                        pubCarAlarmBMOImpl.addPubCarAlarm(PubCarAlarmPo.ERROR_MILEAGE, PubCarAlarmPo.ALARM_LEVEL_MIDDLE, detail, driverName, pubCarTripMileageDto, context);
                    }

                }
            }

        } else if (currentDriverMileages.size() == 1) {
            // 更新里程(结束里程，只支持公里)
            PubCarTripMileageDto driverTripMileage = currentDriverMileages.get(0);
            if (driverTripMileage.getEndMileage() != null || driverTripMileage.getEndTime() != null) {
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "该行程已经结束了");
            }

            driverTripMileage.setEndTime(new Date(currentTime));

            double passMileage = reqJson.getDouble("currentMileage") - driverTripMileage.getStartMileage();

            long diffTime = currentTime - driverTripMileage.getStartTime().getTime();
            long diffMinutes = diffTime / (1000 * 60);
            //long diffHours = diffTime / (60 * 60 * 1000) % 24;
            //long diffDays = diffTime / (24 * 60 * 60 * 1000);

            reqJson.put("duration", (int) diffMinutes);
            reqJson.put("passMileage", passMileage);

            pubCarTripMileageBMOImpl.updateTripMileage(reqJson, context);

            // 异常报警处理
            handleAlarm(diffTime, passMileage, driverTripMileage, context);

            // 查询当前行程的所有车辆行驶信息，如果有一辆车没有结束行程，该行程就仍是进行中，如都结束了行程，该行程才算是结束。
            long count = carTripMileages.stream()
                    .filter(t -> (t.getEndMileage() == null || t.getEndMileage() == 0) || t.getEndTime() == null)
                    .count();

            if (count == 1) {
                // 所有司机都已经结束行程，标记该行程为完成状态
                pubCarTripBMOImpl.updateTripStatus(reqJson, context, StatusConstant.STATUS_FINISHED);
            }

        }
    }




    private void doSavePubCarTripMileageV2(DataFlowContext context, JSONObject reqJson){
        // 查询数据库中是否有了开始里程，若有，则把数据更新到结束里程上
        String tripId = reqJson.getString("tripId");
        String driverId = reqJson.getString("driverId");
        String carId = reqJson.getString("carId");
        String communityId = reqJson.getString("communityId");
        // Long currentTime = reqJson.getLong("currentTime");
        //查询已有的行程
        List<PubCarTripMileageDto> carTripMileages = pubCarTripMileageInnerServiceSMOImpl.queryAllotCarTrip(tripId, communityId);
        List<PubCarTripMileageDto> currentDriverMileages = carTripMileages.stream().filter(t -> driverId.equals(t.getDriverId())).collect(Collectors.toList());

        //2022-5-13 hs: 这里逻辑需要变成开始和结束行程的数据同时提交，所以在进行这个流程时时便需要结束掉当前行程
        //判断行程是否已结束
        if (CollectionUtils.isEmpty(currentDriverMileages)) {

            // PubCarTripMileageDto driverTripMileage = currentDriverMileages.get(0);
            // if (driverTripMileage.getEndMileage() != null || driverTripMileage.getEndTime() != null) {
            //     throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR, "该行程已经结束了");
            // }

            reqJson.put("mileageId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_PUBCAR_TRIP_MILEAGE));
            // 保存图片
            // saveImages(context, reqJson);
            //
            //2022-5-13 hs: 本次行程的里程数等于当前结束行程数减去开始行程数
            double passMileage = reqJson.getDouble("endMileage") - reqJson.getDouble("startMileage");
            long diffTime = reqJson.getLong("endTime") - reqJson.getLong("startTime");
            long diffMinutes = diffTime / (1000 * 60);

            reqJson.put("duration", (int) diffMinutes);
            reqJson.put("passMileage", passMileage);


            // 添加里程(开始里程)
            // 2022-5-13 hs:整合掉了开始行程和结束行程，只要提交了里程便结束掉了行程所以直接同时跟新开始行程和结束行程
            pubCarTripMileageBMOImpl.addTripMileageV2(reqJson, context);
            // 标记行程状态为进行中
            // 2022-5-13 hs:在操作行程详情时就需要结束掉当前行程所以不需要标记为进行中了直接标记为结束
            pubCarTripBMOImpl.updateTripStatus(reqJson, context, StatusConstant.STATUS_FINISHED);
            // 异常报警处理
            // handleAlarm(diffTime, passMileage, driverTripMileage, context);

            // 查询上一部车的结束里程
            Double endMileage = pubCarTripMileageInnerServiceSMOImpl.queryEndMileageOfPreviousCar(carId, communityId);
            if (Objects.nonNull(endMileage)){
                // 2022-5-13 hs:这里需要将当前里程的开始行程数当做前里程的数据
                Double startMileage = reqJson.getDouble("startMileage");
                if (!endMileage.equals(startMileage)){
                    BigDecimal currentDecimal = new BigDecimal(startMileage.toString());
                    BigDecimal endDecimal = new BigDecimal(endMileage.toString());
                    // 误差里程
                    double errorMileage = currentDecimal.subtract(endDecimal).doubleValue();
                    if (errorMileage > 10) {
                        String driverName = userInnerServiceSMOImpl.getUserNameByUserId(driverId);
                        String detail = "上个司机提交的结束里程为：" + endMileage + "公里，当前司机提交的开始里程为：" + startMileage;
                        PubCarTripMileageDto pubCarTripMileageDto = new PubCarTripMileageDto();
                        pubCarTripMileageDto.setDriverId(driverId);
                        pubCarTripMileageDto.setTripId(tripId);
                        pubCarTripMileageDto.setCommunityId(communityId);
                        pubCarAlarmBMOImpl.addPubCarAlarm(PubCarAlarmPo.ERROR_MILEAGE, PubCarAlarmPo.ALARM_LEVEL_MIDDLE, detail, driverName, pubCarTripMileageDto, context);
                    }

                }
            }
        }else{
            //这里为了兼容以前在进行中的行程，如果已存在行程就按照以前的逻辑进行结束行程
            reqJson.put("currentTime",reqJson.getLong("endTime"));
            reqJson.put("currentMileage",reqJson.getDouble("endMileage"));
            doSavePubCarTripMileage(context,reqJson);
        }

    }



    /**
     * 保存图片
     * @param context 上下文
     * @param reqJson 请求json
     */
    private void saveImages(DataFlowContext context, JSONObject reqJson) {
        String imgs = "imgs";
        if (reqJson.containsKey(imgs) && CollectionUtils.isNotEmpty(reqJson.getJSONArray(imgs))){
            JSONArray businessJsonArray = new JSONArray();
            StringJoiner sj = new StringJoiner(",");
            JSONArray imgArray = reqJson.getJSONArray(imgs);

            for (int i = 0, size = imgArray.size(); i < size; i++) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(imgArray.getString(i));
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

                sj.add(fileDto.getFileId());
                reqJson.put("fileSaveName", fileName);

                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setFileRelId("-1");
                fileRelPo.setFileRealName(fileDto.getFileId());
                fileRelPo.setFileSaveName(reqJson.getString("fileSaveName"));
                fileRelPo.setObjId(reqJson.getString("mileageId"));
                fileRelPo.setSaveWay("table");
                fileRelPo.setRelTypeCd("70000");
                JSONObject businessJson = BeanConvertUtil.beanCovertJson(fileRelPo);
                businessJsonArray.add(businessJson);
            }
            reqJson.put("carConditionImg", sj.toString());
            handleBusinessJsonArrayData(context, businessJsonArray, BusinessTypeConstant.BUSINESS_TYPE_SAVE_FILE_REL, FileRelPo.class);
        }
    }

    /**
     * 异常报警有以下几种：
     * (这是第1版本提出的)
     * 1、行程严重超时，实际行程时间超过申请时行程时间的50%即报警。
     * 2、司机未提交里程，司机结束行程回到停车场后超过15分钟未结束行程，提交里程。
     * 3、司机未按时开始行程，司机实际开始行程的时间超过原定开始时间1个小时。
     * 4、行程里程超标，实际行程里程超过原定里程的50%。
     * (这是第2版本提出的)
     *
     * @param diffTime          真实用时
     * @param driverTripMileage driverTripMileage
     */
    private void handleAlarm(long diffTime, double passMileage, PubCarTripMileageDto driverTripMileage, DataFlowContext context) {
        /*Date applyEndTime = driverTripMileage.getApplyEndTime();
        Date applyStartTime = driverTripMileage.getApplyStartTime();
        String detail;*/

        // 行程严重超时
        /*long applyTime = applyEndTime.getTime() - applyStartTime.getTime();
        if (diffTime > applyTime * 1.5) {
            detail = "原定结束时间：" + DateFormatUtils.format(applyEndTime, DateUtil.DATE_FORMATE_STRING_A) +
                    "，实际结束时间：" + DateFormatUtils.format(driverTripMileage.getEndTime(), DateUtil.DATE_FORMATE_STRING_A);
            pubCarAlarmBMOImpl.addPubCarAlarm(PubCarAlarmPo.SEVERE_TIMEOUT, PubCarAlarmPo.ALARM_LEVEL_HIGH, detail, driverTripMileage, context);
        }*/

        // 司机未按时开始行程
        /*Date addOneHourDate = DateUtils.addHours(applyStartTime, 1);
        Date actualStartTime = driverTripMileage.getStartTime();
        if (actualStartTime.after(addOneHourDate)) {
            detail = "原定开始时间：" + DateFormatUtils.format(applyStartTime, DateUtil.DATE_FORMATE_STRING_A) +
                    "，实际开始时间：" + DateFormatUtils.format(driverTripMileage.getStartTime(), DateUtil.DATE_FORMATE_STRING_A);
            pubCarAlarmBMOImpl.addPubCarAlarm(PubCarAlarmPo.NOT_START_TRIP_ON_TIME, PubCarAlarmPo.ALARM_LEVEL_MIDDLE, detail, driverTripMileage, context);
        }*/

        // 行程里程超标
        /*Double applyMileage = driverTripMileage.getApplyMileage();
        if (passMileage > applyMileage * 1.5) {
            detail = "原定里程：" + applyMileage +
                    "公里，实际里程：" + passMileage + "公里";
            pubCarAlarmBMOImpl.addPubCarAlarm(PubCarAlarmPo.MILEAGE_EXCEEDED, PubCarAlarmPo.ALARM_LEVEL_HIGH, detail, driverTripMileage, context);
        }*/

    }

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripMileageConstant.START_END_PUBCARTRIPMILEAGE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}

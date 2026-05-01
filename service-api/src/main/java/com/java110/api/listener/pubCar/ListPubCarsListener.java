package com.java110.api.listener.pubCar;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.intf.pubcar.IPubCarInnerServiceSMO;
import com.java110.utils.constant.ServiceCodePubCarConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.dto.pubCar.PubCarDto;
import com.java110.utils.util.ConstantUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.pubCar.*;
import com.java110.vo.api.pubCarArchives.ApiPubCarArchivesDataVo;
import com.java110.vo.api.pubCarArchives.ApiPubCarArchivesVo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * 查询公车信息侦听类
 */
@Java110Listener("listPubCarsListener")
public class ListPubCarsListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarInnerServiceSMO pubCarInnerServiceSMOImpl;

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarConstant.LIST_PUBCARS;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    protected void validate(ServiceDataFlowEvent event, JSONObject reqJson) {}

    @Override
    protected void doSoService(ServiceDataFlowEvent event, DataFlowContext context, JSONObject reqJson) {

        if (reqJson.containsKey(ConstantUtil.SIMPLE_FLAG)){
            // 获取车辆的简单信息，不获取全部信息
            if (ConstantUtil.SIMPLE_FLAG_YES.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))) {
                getSimpleCarInfo(context, reqJson);
                return;
            }

            // 获取车辆统计信息
            if (ConstantUtil.SIMPLE_FLAG_STATICS.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))) {
                getCarStatistics(context, reqJson);
                return;
            }

            // 获取车辆档案信息
            if (ConstantUtil.SIMPLE_FLAG_CAR_ARCHIVES.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))) {
                getPubCarArchives(context, reqJson);
                return;
            }
        }

        PubCarDto pubCarDto = BeanConvertUtil.covertBean(reqJson, PubCarDto.class);

        int count = pubCarInnerServiceSMOImpl.queryPubCarsCount(pubCarDto);

        List<ApiPubCarDataVo> pubCars;

        if (count > 0) {
            pubCars = covertApiPubCarDataVoList(pubCarInnerServiceSMOImpl.queryPubCars(pubCarDto));
        } else {
            pubCars = Collections.emptyList();
        }

        ApiPubCarVo apiPubCarVo = new ApiPubCarVo();

        apiPubCarVo.setTotal(count);
        apiPubCarVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarVo.setData(pubCars);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 获取车辆档案信息
     * @param context
     * @param reqJson
     */
    private void getPubCarArchives(DataFlowContext context, JSONObject reqJson) {
        PubCarDto pubCarDto = BeanConvertUtil.covertBean(reqJson, PubCarDto.class);

        int count = pubCarInnerServiceSMOImpl.queryPubCarArchivesCount(pubCarDto);

        List<ApiPubCarArchivesDataVo> pubCarArchives;

        if (count > 0) {
            pubCarArchives = pubCarInnerServiceSMOImpl.queryPubCarArchives(pubCarDto);
        } else {
            pubCarArchives = Collections.emptyList();
        }

        ApiPubCarArchivesVo apiPubCarArchivesVo = new ApiPubCarArchivesVo();

        apiPubCarArchivesVo.setTotal(count);
        apiPubCarArchivesVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarArchivesVo.setData(pubCarArchives);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarArchivesVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 获取车辆统计信息列表
     *
     * @param context
     * @param reqJson
     */
    private void getCarStatistics(DataFlowContext context, JSONObject reqJson) {
        PubCarDto pubCarDto = BeanConvertUtil.covertBean(reqJson, PubCarDto.class);

        int count = pubCarInnerServiceSMOImpl.queryCarStatisticsCount(pubCarDto);

        List<ApiCarStatisticsDataVo> carStatistics;

        if (count > 0) {
            carStatistics = pubCarInnerServiceSMOImpl.queryCarStatistics(pubCarDto);
        } else {
            carStatistics = Collections.emptyList();
        }

        ApiCarStatisticsVo apiCarStatisticsVo = new ApiCarStatisticsVo();

        apiCarStatisticsVo.setTotal(count);
        apiCarStatisticsVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCarStatisticsVo.setData(carStatistics);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiCarStatisticsVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 获取车辆的简单信息
     *
     * @param context
     */
    private void getSimpleCarInfo(DataFlowContext context, JSONObject reqJson) {
        String startTime = reqJson.getString("startTime");
        String endTime = reqJson.getString("endTime");
        Integer carType = reqJson.getInteger("carType");
        String communityId = reqJson.getString("communityId");
        List<ApiSimplePubCarDataVo> simplePubCars = pubCarInnerServiceSMOImpl.querySimplePubCars(communityId, startTime, endTime, carType);

        ApiSimplePubCarVo apiSimplePubCarVo = new ApiSimplePubCarVo();
        apiSimplePubCarVo.setTotal(simplePubCars.size());
        apiSimplePubCarVo.setRecords(0);
        apiSimplePubCarVo.setData(simplePubCars);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiSimplePubCarVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 拷贝属性
     *
     * @param queryPubCars 查询到的公车列表
     * @return
     */
    private List<ApiPubCarDataVo> covertApiPubCarDataVoList(List<PubCarDto> queryPubCars) {
        List<ApiPubCarDataVo> apiPubCarDataVos = new ArrayList<>();
        for (PubCarDto queryPubCar : queryPubCars) {
            ApiPubCarDataVo apiPubCarDataVo = new ApiPubCarDataVo();
            BeanUtils.copyProperties(queryPubCar, apiPubCarDataVo);
            putCatTypeName(apiPubCarDataVo);

            // 拷贝attr
            ApiPubCarAttrDataVo apiPubCarAttrDataVo = new ApiPubCarAttrDataVo();
            BeanUtils.copyProperties(queryPubCar, apiPubCarAttrDataVo);
            putAccidentSituationName(apiPubCarAttrDataVo);
            // 处理日期格式
            if (Objects.nonNull(queryPubCar.getRegisterDate())){
                apiPubCarAttrDataVo.setRegisterDate(DateFormatUtils.format(queryPubCar.getRegisterDate(), DATE_FORMAT));
            }
            apiPubCarDataVo.setAttr(apiPubCarAttrDataVo);

            // 拷贝insurance
            ApiPubCarInsuranceDataVo apiPubCarInsuranceDataVo = new ApiPubCarInsuranceDataVo();
            Date quarterReview = queryPubCar.getQuarterReview();
            Date yearReview = queryPubCar.getYearReview();
            Date insuranceExpirationDate = queryPubCar.getInsuranceExpirationDate();
            Date maintenanceExpirationDate = queryPubCar.getMaintenanceExpirationDate();

            if (Objects.nonNull(quarterReview)){
                apiPubCarInsuranceDataVo.setQuarterReview(DateFormatUtils.format(quarterReview, DATE_FORMAT));
            }
            if (Objects.nonNull(yearReview)){
                apiPubCarInsuranceDataVo.setYearReview(DateFormatUtils.format(yearReview, DATE_FORMAT));
            }
            if (Objects.nonNull(insuranceExpirationDate)){
                // 计算保险还有多少天过期
                Date nowDate = new Date();
                if (insuranceExpirationDate.after(nowDate)){
                    int daysBetween = DateUtil.daysBetween(insuranceExpirationDate, nowDate);
                    apiPubCarInsuranceDataVo.setRemainExpirationDays(daysBetween);
                }else {
                    apiPubCarInsuranceDataVo.setRemainExpirationDays(0);
                }
                apiPubCarInsuranceDataVo.setInsuranceExpirationDate(DateFormatUtils.format(insuranceExpirationDate, DATE_FORMAT));
            }
            if (Objects.nonNull(maintenanceExpirationDate)){
                apiPubCarInsuranceDataVo.setMaintenanceExpirationDate(DateFormatUtils.format(maintenanceExpirationDate, DATE_FORMAT));
            }

            apiPubCarInsuranceDataVo.setQuarterReviewContent(queryPubCar.getQuarterReviewContent());
            apiPubCarInsuranceDataVo.setYearReviewContent(queryPubCar.getYearReviewContent());
            apiPubCarInsuranceDataVo.setInsuranceCompany(queryPubCar.getInsuranceCompany());
            apiPubCarInsuranceDataVo.setMaintenanceFee(queryPubCar.getMaintenanceFee());
            apiPubCarInsuranceDataVo.setMileage(queryPubCar.getMileage());
            apiPubCarDataVo.setInsurance(apiPubCarInsuranceDataVo);

            apiPubCarDataVos.add(apiPubCarDataVo);
        }

        return apiPubCarDataVos;
    }

    /**
     * 事故情况。1：未发生过致人伤亡事故，2：发生过致人伤亡事故。
     * @param apiPubCarAttrDataVo 数据
     */
    private void putAccidentSituationName(ApiPubCarAttrDataVo apiPubCarAttrDataVo) {
        Integer accidentSituation = apiPubCarAttrDataVo.getAccidentSituation();
        if (Objects.isNull(accidentSituation)){
            return;
        }

        switch (accidentSituation){
            case 1: apiPubCarAttrDataVo.setAccidentSituationName("未发生过致人伤亡事故");break;
            case 2: apiPubCarAttrDataVo.setAccidentSituationName("发生过致人伤亡事故");break;
            default:
        }
    }

    /**
     * 车辆类型。1：7座以下小客车，2：7座（含）以上小客车；3：货车，4：摩托车，5：其它机动车。
     * @param apiPubCarDataVo 数据
     */
    private void putCatTypeName(ApiPubCarDataVo apiPubCarDataVo) {
        Integer carType = apiPubCarDataVo.getCarType();
        if (Objects.isNull(carType)){
            return;
        }

        switch (carType){
            case 1: apiPubCarDataVo.setCarTypeName("7座以下小客车");break;
            case 2: apiPubCarDataVo.setCarTypeName("7座（含）以上小客车");break;
            case 3: apiPubCarDataVo.setCarTypeName("货车");break;
            case 4: apiPubCarDataVo.setCarTypeName("摩托车");break;
            case 5: apiPubCarDataVo.setCarTypeName("其它机动车");break;
            default:
        }
    }

}

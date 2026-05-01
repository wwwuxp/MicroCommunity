package com.java110.api.listener.pubCarTrip;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.event.service.api.ServiceDataFlowEvent;
import com.java110.dto.pubCarApprovalProcess.PubCarApprovalProcessDto;
import com.java110.dto.pubCarTrip.PubCarTripDto;
import com.java110.dto.pubCarTrip.PubCarTripSimpleDto;
import com.java110.intf.pubcar.IPubCarApprovalProcessInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarApproverInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarTripInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.pubCarApprovalProcess.PubCarApprovalProcessPo;
import com.java110.po.pubCarApprover.PubCarApproverPo;
import com.java110.utils.constant.ServiceCodePubCarTripConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ConstantUtil;
import com.java110.vo.api.pubCarTrip.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询行程信息侦听
 */
@Java110Listener("listPubCarTripsListener")
public class ListPubCarTripsListener extends AbstractServiceApiListener {

    @Autowired
    private IPubCarTripInnerServiceSMO pubCarTripInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IPubCarApprovalProcessInnerServiceSMO pubCarApprovalProcessInnerServiceSMOImpl;

    @Autowired
    private IPubCarApproverInnerServiceSMO pubCarApproverInnerServiceSMOImpl;

    @Override
    public String getServiceCode() {
        return ServiceCodePubCarTripConstant.LIST_PUBCARTRIPS;
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
            // 根据司机id获取详细的司机行程信息
            if (ConstantUtil.SIMPLE_FLAG_YES.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))){
                getDriverTripDetailInfo(context, reqJson);
                return;
            }

            // 根据 司机id或者部门id 查询简单的行程信息，对应于司机用车统计-详情、部门用车统计-详情
            if (ConstantUtil.SIMPLE_FLAG_STATICS.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))){
                getTripSimpleInfo(context, reqJson);
                return;
            }

            // 根据 车辆id 查询车辆的行程信息
            if (ConstantUtil.SIMPLE_FLAG_STATICS_CAR.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))){
                getCarTripInfo(context, reqJson);
                return;
            }

            // 移动端用车事故 - 选择对应的行程
            if (ConstantUtil.SIMPLE_FLAG_TRIP_CHOOSE.equals(reqJson.getString(ConstantUtil.SIMPLE_FLAG))){
                chooseTrip(context, reqJson);
                return;
            }
        }

        List<ApiPubCarTripDataVo> trips;
        int count;
        PubCarTripDto pubCarTripDto = BeanConvertUtil.covertBean(reqJson, PubCarTripDto.class);
        // 判断是否是管理员账号，是管理员账号则可以看所有的数据
        boolean isAdministrator = userInnerServiceSMOImpl.isAdministrator(context.getUserId());
        if (isAdministrator){

            count = pubCarTripInnerServiceSMOImpl.queryPubCarTripsCount(pubCarTripDto);

            if (count > 0) {
                trips = pubCarTripInnerServiceSMOImpl.queryPubCarTrips(pubCarTripDto);

                for (ApiPubCarTripDataVo trip : trips) {
                    trip.setReviewerRole(PubCarApprovalProcessPo.PUBCAR_ADMINISTRATOR);
                }
            } else {
                trips = Collections.emptyList();
            }
        }else {
            // 判断是否是用车审批人，如果不是用车审批人，说明是普通员工
            boolean isApprover = pubCarApproverInnerServiceSMOImpl.isApprover(context.getUserId(), PubCarApproverPo.TYPE_PUBCAR);
            if (isApprover) {
                // 是用车审批人
                // 非管理员，只能查看自己账号下是否有需要审核的行程信息
                PubCarApprovalProcessDto pubCarApprovalProcessDto = BeanConvertUtil.covertBean(reqJson, PubCarApprovalProcessDto.class);
                pubCarApprovalProcessDto.setCurrentUserId(context.getUserId());

                count = pubCarApprovalProcessInnerServiceSMOImpl.getTripIdsCount(pubCarApprovalProcessDto);
                if (count > 0) {
                    List<PubCarApprovalProcessDto> pubCarApprovalProcessDtos = pubCarApprovalProcessInnerServiceSMOImpl.getTrips(pubCarApprovalProcessDto);
                    Map<String, Integer> tripIdReviewerRoleMap =
                            pubCarApprovalProcessDtos.stream().collect(Collectors.toMap(PubCarApprovalProcessDto::getTripId, PubCarApprovalProcessDto::getReviewerRole, (k1, k2) -> k2));
                    Map<String, Integer> tripIdApprovalStatusMap = pubCarApprovalProcessDtos.stream().collect(Collectors.toMap(PubCarApprovalProcessDto::getTripId, PubCarApprovalProcessDto::getApprovalStatus, (k1, k2) -> k2));

                    Set<String> tripIdSet = tripIdReviewerRoleMap.keySet();
                    if (CollectionUtils.isNotEmpty(tripIdSet)) {
                        ArrayList<String> tripIds = new ArrayList<>(tripIdSet);
                        // 查找行程id的具体信息
                        pubCarTripDto.setTripIds(tripIds);
                        pubCarTripDto.setApprovalStatus(null);
                        trips = pubCarTripInnerServiceSMOImpl.queryPubCarTrips(pubCarTripDto);

                        for (ApiPubCarTripDataVo trip : trips) {
                            String tripId = trip.getTripId();
                            trip.setReviewerRole(tripIdReviewerRoleMap.get(tripId));
                            trip.setApprovalStatus(tripIdApprovalStatusMap.get(tripId));
                        }
                    }else {
                        trips = Collections.emptyList();
                    }
                } else {
                    trips = Collections.emptyList();
                }
            }else {
                // 不是用车审批人
                pubCarTripDto.setUserId(context.getUserId());
                count = pubCarTripInnerServiceSMOImpl.queryPubCarTripsCount(pubCarTripDto);

                if (count > 0) {
                    trips = pubCarTripInnerServiceSMOImpl.queryPubCarTrips(pubCarTripDto);

                    for (ApiPubCarTripDataVo trip : trips) {
                        trip.setReviewerRole(PubCarApprovalProcessPo.GENERAL_STAFF);
                    }
                } else {
                    trips = Collections.emptyList();
                }
            }
        }


        ApiPubCarTripVo apiPubCarTripVo = new ApiPubCarTripVo();

        apiPubCarTripVo.setTotal(count);
        apiPubCarTripVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPubCarTripVo.setData(trips);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarTripVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);

    }

    /**
     * 移动端用车事故 - 选择对应的行程
     * @param context context
     * @param reqJson reqJson
     */
    private void chooseTrip(DataFlowContext context, JSONObject reqJson) {
        PubCarTripSimpleDto pubCarTripSimpleDto = BeanConvertUtil.covertBean(reqJson, PubCarTripSimpleDto.class);

        int count = pubCarTripInnerServiceSMOImpl.listChooseTripsCount(pubCarTripSimpleDto);

        List<ApiChooseTripDataVo> chooseTripDataVos;

        if (count > 0) {
            chooseTripDataVos = pubCarTripInnerServiceSMOImpl.listChooseTrips(pubCarTripSimpleDto);
        } else {
            chooseTripDataVos = Collections.emptyList();
        }

        ApiChooseTripVo chooseTripVo = new ApiChooseTripVo();

        chooseTripVo.setTotal(count);
        if (Objects.isNull(reqJson.getInteger("row"))){
            chooseTripVo.setRecords(1);
        }else {
            chooseTripVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        }
        chooseTripVo.setData(chooseTripDataVos);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(chooseTripVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 车辆统计中根据车辆id获取车辆的行程信息
     * @param context context
     * @param reqJson reqJson
     */
    private void getCarTripInfo(DataFlowContext context, JSONObject reqJson) {
        PubCarTripSimpleDto pubCarTripSimpleDto = BeanConvertUtil.covertBean(reqJson, PubCarTripSimpleDto.class);

        int count = pubCarTripInnerServiceSMOImpl.queryCarTripStatisticsCount(pubCarTripSimpleDto);

        List<ApiCarTripStatisticsDataVo> trips;

        if (count > 0) {
            trips = pubCarTripInnerServiceSMOImpl.queryCarTripStatistics(pubCarTripSimpleDto);
        } else {
            trips = Collections.emptyList();
        }

        ApiCarTripStatisticsVo apiCarTripStatisticsVo = new ApiCarTripStatisticsVo();

        apiCarTripStatisticsVo.setTotal(count);
        apiCarTripStatisticsVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiCarTripStatisticsVo.setData(trips);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiCarTripStatisticsVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 司机用车统计中根据司机id查询行程信息
     * 部门用车统计中根据部门id查询行程信息
     * @param context context
     * @param reqJson reqJson
     */
    private void getTripSimpleInfo(DataFlowContext context, JSONObject reqJson) {
        PubCarTripSimpleDto pubCarTripSimpleDto = BeanConvertUtil.covertBean(reqJson, PubCarTripSimpleDto.class);

        int count = pubCarTripInnerServiceSMOImpl.queryPubCarSimpleTripsCount(pubCarTripSimpleDto);

        List<ApiSimpleTripDataVo> trips;

        if (count > 0) {
            trips = pubCarTripInnerServiceSMOImpl.queryPubCarSimpleTrips(pubCarTripSimpleDto);
        } else {
            trips = Collections.emptyList();
        }

        ApiSimpleTripVo apiSimpleTripVo = new ApiSimpleTripVo();

        apiSimpleTripVo.setTotal(count);
        apiSimpleTripVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiSimpleTripVo.setData(trips);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiSimpleTripVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    /**
     * 获取司机行程详细信息
     * @param context context
     * @param reqJson reqJson
     */
    private void getDriverTripDetailInfo(DataFlowContext context, JSONObject reqJson) {
        PubCarTripSimpleDto pubCarTripSimpleDto = BeanConvertUtil.covertBean(reqJson, PubCarTripSimpleDto.class);

        int count = pubCarTripInnerServiceSMOImpl.queryPubCarDriverTripsCount(pubCarTripSimpleDto);

        List<ApiPubCarTripDataVo> driverTrips;

        if (count > 0){
            driverTrips = pubCarTripInnerServiceSMOImpl.queryPubCarDriverTrips(pubCarTripSimpleDto);
        }else {
            driverTrips = Collections.emptyList();
        }

        ApiPubCarTripVo apiPubCarTripVo = new ApiPubCarTripVo();

        apiPubCarTripVo.setTotal(count);
        apiPubCarTripVo.setRecords((int) Math.ceil((double) driverTrips.size() / (double) reqJson.getInteger("row")));
        apiPubCarTripVo.setData(driverTrips);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(JSONObject.toJSONString(apiPubCarTripVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}

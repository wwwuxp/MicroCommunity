package com.java110.core.component;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ConstantUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

/**
 * 组件抽象类
 * <p>
 * add by wuxw 2019-06-19
 */
public abstract class AbstractComponentSMO extends BaseComponentSMO {
    private static Logger logger = LoggerFactory.getLogger(AbstractComponentSMO.class);


    /**
     * 统一业务处理类
     *
     * @param pd 页面数据封装
     * @return ResponseEntity对象
     */
    protected final ResponseEntity<String> businessProcess(IPageData pd) {

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        logger.debug("businessProcess: pd=" + JSONObject.toJSONString(pd));


        //业务数据校验
        validate(pd, paramIn);
        ResponseEntity<String> businessResult = null;
        try {
            businessResult = doBusinessProcess(pd, paramIn);
        } catch (Exception e) {
            logger.error("调用实现类异常：", e);
            businessResult = new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }

        return businessResult;

    }

    /**
     * 页面数据校验 方法
     *
     * @param pd      页面数据封装
     * @param paramIn 前台数据对象
     */
    protected abstract void validate(IPageData pd, JSONObject paramIn);

    /**
     * 业务数据处理类
     *
     * @param pd      页面数据封装
     * @param paramIn 前台数据对象
     */
    protected abstract ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws Exception;


    //    =============================
    //    =    以下是二次开发新增的方法    =
    //    =============================

    /**
     * 校验车辆
     *
     * @param paramIn 请求参数
     */
    protected void validatePubCar(JSONObject paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "license", "请填写车牌号");
        Assert.jsonObjectHaveKey(paramIn, "brand", "请填写品牌");
        Assert.jsonObjectHaveKey(paramIn, "seatNum", "请填写座位数");
        if (!paramIn.getString("seatNum").matches(ConstantUtil.REGEX_NUMBER)) {
            throw new IllegalArgumentException("非法座位数");
        }
        Assert.jsonObjectHaveKey(paramIn, "carType", "请选择车辆类型");
        Assert.jsonObjectHaveKey(paramIn, "carType1", "请选择是执勤车还是公务车");
        Assert.jsonObjectHaveKey(paramIn, "attr", "请求报文中未包含attr");
        Assert.jsonObjectHaveKey(paramIn, "insurance", "请求报文中未包含insurance");
        // Assert.jsonObjectHaveKey((JSONObject) paramIn.get("attr"), "registerDate", "请填写车辆注册日期");
        // Assert.jsonObjectHaveKey((JSONObject) paramIn.get("attr"), "accidentSituation", "请选择事故情况");
        Assert.jsonObjectHaveKey(paramIn, "communityId", "communityId 参数不能为空");
    }

    /**
     * 校验司机
     *
     * @param paramIn 请求参数
     */
    protected void validateDriver(JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "name", "请填写姓名");
        Assert.hasKeyAndValue(paramIn, "gender", "请选择性别");
        Assert.hasKeyAndValue(paramIn, "departmentId", "请选择部门");
        Assert.hasKeyAndValue(paramIn, "positionId", "请选择职位");
        Assert.hasKeyAndValue(paramIn, "tel", "请填写电话");
        // 电话号码是否符合要求
        if (!paramIn.getString("tel").matches(ConstantUtil.REGEX_TEL)) {
            throw new IllegalArgumentException("电话号码格式不正确");
        }
        Assert.hasKeyAndValue(paramIn, "jobNum", "请填写工号");

        if (paramIn.containsKey("idNum") && StringUtils.isNotBlank(paramIn.getString("idNum")) && !paramIn.getString("idNum").matches(ConstantUtil.REGEX_ID_NUM)) {
            throw new IllegalArgumentException("身份证号码格式不正确");
        }

        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
    }

    /**
     * 校验用车人
     *
     * @param paramIn 请求参数
     */
    protected void validatePubCarUser(JSONObject paramIn) {
        if (paramIn.containsKey("carUserId")) {
            Assert.hasKeyAndValue(paramIn, "userId", "请选择一个用车人进行更新");
        } else {
            Assert.hasKey(paramIn, "userIds", "userIds 节点不存在");
            JSONArray userIds = paramIn.getJSONArray("userIds");
            if (CollectionUtils.isEmpty(userIds)) {
                throw new IllegalArgumentException("请选择用车人");
            }
        }
        Assert.hasKeyAndValue(paramIn, "isSelfDriving", "是否是自驾参数不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
    }

    /**
     * 校验审核信息
     *
     * @param paramIn 请求参数
     */
    protected void validateAuditTrip(JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "tripId", "行程id不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
        Assert.hasKeyAndValue(paramIn, "state", "审核状态不能为空");

        // 审核不通过时请求中可以不用传递车辆信息（info节点）
        Integer state = paramIn.getInteger("state");
        if (StatusConstant.STATUS_NOT_STARTED.equals(state)) {
            return;
        }

        Assert.hasKey(paramIn, "info", "当前为审核通过状态，但请求中没有包含info节点");
        JSONArray infoArray = paramIn.getJSONArray("info");
        if (CollectionUtils.isEmpty(infoArray)) {
            throw new IllegalArgumentException("当前为审核通过状态，但info节点中未包含任何信息");
        }

        for (int i = 0; i < infoArray.size(); i++) {
            JSONObject oneInfoJson = infoArray.getJSONObject(i);
            Assert.hasKeyAndValue(oneInfoJson, "carId", "存在未选择车辆");
            Assert.hasKeyAndValue(oneInfoJson, "driverId", "存在未选择司机");
            Assert.hasKeyAndValue(oneInfoJson, "no", "未传入车辆编号no");
        }
    }

    /**
     * 校验违章信息
     *
     * @param paramIn 请求参数
     */
    protected void validateViolation(JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "date", "违章日期不能为空");
        Assert.hasKeyAndValue(paramIn, "address", "违章地址不能为空");
        Assert.hasKeyAndValue(paramIn, "content", "违法具体内容不能为空");
        Assert.hasKeyAndValue(paramIn, "driver", "驾驶人不能为空");
        Assert.hasKeyAndValue(paramIn, "carId", "车辆id不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");

        if (StringUtils.isNotBlank(paramIn.getString("deduction"))) {
            String deduction = paramIn.getString("deduction");
            if (!deduction.matches(ConstantUtil.REGEX_NUMBER)){
                throw new IllegalArgumentException("输入的扣分数据应为数字");
            }
        }else {
            paramIn.put("deduction", 0);
        }

        if (StringUtils.isNotBlank(paramIn.getString("fine"))){
            String fine = paramIn.getString("fine");
            if (!fine.matches(ConstantUtil.REGEX_DECIMAL)){
                throw new IllegalArgumentException("输入的罚款金额格式不正确");
            }
        }else {
            paramIn.put("fine", new BigDecimal("0.00"));
        }
    }

    /**
     * 校验维修信息
     *
     * @param paramIn 请求参数
     */
    protected void validateRepair(JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "date", "请填写维修日期");
        Assert.hasKeyAndValue(paramIn, "content", "请填写维修内容");
        Assert.hasKeyAndValue(paramIn, "price", "请填写维修价格");
        if (!paramIn.getString("price").matches(ConstantUtil.REGEX_DECIMAL)) {
            throw new IllegalArgumentException("输入的维修价格格式不正确");
        }
        Assert.hasKeyAndValue(paramIn, "carId", "车辆id不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
    }

    /**
     * 校验报警信息
     *
     * @param paramIn 请求参数
     */
    protected void validateAlarm(JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "alarmId", "异常报警信息ID不能为空");
        Assert.hasKeyAndValue(paramIn, "resultProcess", "异常处理结果不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
    }

    /**
     * 校验分组
     *
     * @param paramIn 请求参数
     */
    protected void validateGroup(JSONObject paramIn) {
        Assert.hasKeyAndValue(paramIn, "groupType", "分组类型不能为空");
        Assert.hasKeyAndValue(paramIn, "groupName", "分组名不能为空");
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId 参数不能为空");
    }


}

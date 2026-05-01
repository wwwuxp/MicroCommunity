package com.java110.utils.util;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 常量定义管理类
 * Created by wuxw on 2017/3/14.
 */
public class ConstantUtil {

    public static final LocalTime MORNING_START_TIME = LocalTime.parse("07:50:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
    public static final LocalTime MORNING_END_TIME = LocalTime.parse("09:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
    public static final LocalTime NOON_START_TIME = LocalTime.parse("11:50:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
    public static final LocalTime NOON_END_TIME = LocalTime.parse("13:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
    public static final LocalTime AFTERNOON_START_TIME = LocalTime.parse("17:50:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
    public static final LocalTime AFTERNOON_END_TIME = LocalTime.parse("19:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));

    /**
     * 上午上班时间
     */
    public static final LocalTime MORNING_WORKING_HOURS = LocalTime.parse("09:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
    /**
     * 上午下班时间
     */
    public static final LocalTime MORNING_OFF_HOURS = LocalTime.parse("12:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));

    /**
     * 下午上班时间
     */
    public static final LocalTime WORKING_HOURS_IN_THE_AFTERNOON = LocalTime.parse("14:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
    /**
     * 下午下班时间
     */
    public static final LocalTime AFTERNOON_GET_OFF_WORK_HOURS = LocalTime.parse("18:00:00", DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));

    /**
     * 上班异常临界时间
     */
    public static final String FIXED_START_TIME = "09:05:59";

    /**
     * 下班异常临界时间
     */
    public static final String FIXED_END_TIME = "17:55:00";

    /**
     * 上午下班异常临界时间
     */
    public static final String MORNING_FIXED_END_TIME = "11:55:00";

    /**
     * 下午上班异常临界时间
     */
    public static final String NOON_FIXED_START_TIME = "14:05:59";

    /**
     * 经纬度正则表达式
     */
    public static final String REGEX_LOCATION = "^((\\-|\\+)?(((\\d|[1-9]\\d|1[0-7]\\d|0{1,3})\\.\\d{0,6})|(\\d|[1-9]\\d|1[0-7]\\d|0{1,3})|180\\.0{0,6}|180)),((\\-|\\+)?([0-8]?\\d{1}\\.\\d{0,6}|90\\.0{0,6}|[0-8]?\\d{1}|90))$";

    /**
     * 电话号码正则表达式
     */
    public static final String REGEX_TEL = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";

    /**
     * 身份证号码正则表达式
     */
    public static final String REGEX_ID_NUM ="(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

    /**
     * 金钱或里程正则表达式
     */
    public static final String REGEX_DECIMAL = "^[0-9]+(\\.[0-9]{1,2})?$";

    /**
     * 数字正则表达式
     */
    public static final String REGEX_NUMBER = "\\d+";

    /**
     * 明天
     */
    public static final Integer TOMORROW = 1;

    /**
     * 未来七天
     */
    public static final Integer ONEWEEK = 2;

    /**
     * 标记信息
     */
    public static final String SIMPLE_FLAG = "simple_flag";
    public static final String SIMPLE_FLAG_YES = "Y";
    public static final String SIMPLE_FLAG_NO = "N";
    public static final String SIMPLE_FLAG_STATICS = "S";
    public static final String SIMPLE_FLAG_STATICS_CAR = "C";
    public static final String SIMPLE_FLAG_DEPARTMENT_NAME = "DN";
    public static final String SIMPLE_FLAG_TRIP_CHOOSE = "TC";
    /**
     * 车辆档案
     */
    public static final String SIMPLE_FLAG_CAR_ARCHIVES = "A";

    /**
     * 能力平台请求时的授权码
     */
    public static final String SAOP_SRC_SYS_SIGN="SrcSysSign";

    /**
     * 能力平台源组织ID
     */
    public static final String SAOP_SRC_ORG_ID="SrcOrgID";

    /**
     * 源系统ID
     */
    public static final String SAOP_SRC_SYS_ID="SrcSysID";

    /**
     * 目标组织ID
     */
    public static final String SAOP_DST_ORG_ID = "DstOrgID";
    /**
     * 目标系统ID
     */
    public static final String SAOP_DST_SYS_ID = "DstSysID";

    /**
     * 渠道编码
     */
    public static final String SAOP_CHANNEL_NBR = "CHANNEL_NBR";
    /**
     * 员工编码
     */
    public static final String SAOP_STAFF_CODE = "STAFF_CODE";


    /**
     * 根据号码查询用户信息报文体获取
     */
    public static final String SAOP_GET_CUST_INFO_BY_ACC_NBR = "getCustInfoListByAccNbr";

    /**
     * 能力向省内提供接口地址
     */
    public static final String SAOP_PROVINCE_WEBSERVICE_URL = "saop_province_webservice_url";

    /**
     * 增加购物车属性配置，这里只支持固定值得
     */
    public static final String SAOP_ADD_CUSTOMER_ORDER_ATTR = "ADD_CUSTOMER_ORDER_ATTR";

    /**
     * 修改购物车属性配置，这里只支持固定值得
     */
    public static final String SAOP_MOD_CUSTOMER_ORDER_ATTR = "MOD_CUSTOMER_ORDER_ATTR";


    /**
     * 能力平台向省内提供的接口方法
     */
    public static final String SAOP_PROVINCE_FUNCTION = "exchangeForProvince";

    public static final String SAOP_EXCHANGE_FUNCTION = "exchange";

    /**
     * 短信域，只要用户发送验证码
     */
    public static final String DOMAIN_SMS = "sms";

    /**
     * 短信相关信息
     */
    public static final String SMS_USERNAME = "username";
    public static final String SMS_PASSWORD = "password";
    public static final String SMS_SYSCODE = "syscode";
    public static final String SMS_PRODUCT_ID = "productId";
    public static final String SMS_CHANNEL_TYPE = "channelType";
    public static final String SMS_FLOW_CODE = "flowCode";
    public static final String SMS_SENT_TYPE = "sentType";
    public static final String SMS_BUSINESS_ID = "businessId";
    public static final String SMS_CREATE_DESPART = "createDespart";
    public static final String SMS_CREATE_STAFF = "createStaff";
    public static final String SMS_PLAN_ID = "planId";
    public static final String SMS_PUSH_URL = "pushUrl";

    /**
     * 短信验证码，短信模板
     */
    public static final String SMS_SEND_CODE_CONTENT="sendCodeContent";

    /**
     * 短信平台接口地址
     */
    public static final String SMS_SEND_CODE_WEBSERVICE_URL = "sms_send_code_webservice_url";

    /**
     * 短信平台接口方法
     */
    public static final String SMS_SEND_CODE_FUNCTION = "sms_send_code_function";


    //服务交互报文保存开关 ON Or OFF
    public static final String SAVE_SEVICE_LOG_FLAG = "save_sevice_log_flag";


    /**
     * 销售品订购，退订 调用4G接口
     */
    public static final String OFFER_CATALOG_4G = "4G";


    /**
     * 销售品订购，退订 调用3G接口
     */
    public static final String OFFER_CATALOG_3G = "3G";


    /**
     * 销售品订购，退订 调用EOP接口
     */
    public static final String OFFER_CATALOG_EOP = "EOP";

}

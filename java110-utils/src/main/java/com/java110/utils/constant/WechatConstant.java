package com.java110.utils.constant;

/**
 * @ClassName WechatConstant
 * @Description TODO 微信相关常量类
 * @Author wuxw
 * @Date 2020/6/13 22:01
 * @Version 1.0
 * add by wuxw 2020/6/13
 **/
public class WechatConstant {

    /**
     * new add
     * 获取access_token
     */
    public static final String GET_QY_ACCESS_TOKEN = "http://10.2.14.201/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET";

    /**
     * new add
     * 根据code码获取用户信息
     */
    public static final String GET_QY_USER_ID = "http://10.2.14.201/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";

    /**
     * new add
     * 企业微信h5认证
     */
    public static final String QY_WEB_AUTH = "http://58.251.74.7/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&agentid=AGENTID&state=STATE#wechat_redirect";

    //微信域
    public static final String WECHAT_DOMAIN = "WECHAT";

    public static final String GET_USER_URL = "GET_USER_URL";
    public static final String SEND_TEMPLATE_URL = "SEND_TEMPLATE_URL";
    public static final String GET_ACCESS_TOKEN_URL = "GET_ACCESS_TOKEN_URL";

    //微信ID
    public static final String PAGE_WECHAT_APP_ID = "wId";//微信id 一般是appId

    public static final String KEY_PROPERTY_FEE_TEMPLATE_ID = "PROPERTY_FEE_TEMPLATE_ID";

    public static final String PAY_GOOD_NAME = "PAY_GOOD_NAME";

    public static final String TOKEN = "TOKEN";
    public static final String WELCOME = "WELCOME";
    public static final String NO_BIND_OWNER = "NO_BIND_OWNER"; // 未绑定业主

    public static final String DEFAULT_WELCOME = "欢迎来到智慧物业系统";

    public static final String NO_BIND_OWNER_RESPONSE_MESSAGE = "亲，您还没有绑定业主";

    public static final String OWE_FEE_PAGE = "OWE_FEE_PAGE";

    public static final String OWE_CAR_FEE_PAGE = "OWE_CAR_FEE_PAGE";


    public static final String MSG_TYPE_TEXT = "text";
    public static final String MSG_TYPE_LOCATION = "location";
    public static final String MSG_TYPE_VOICE = "voice";
    public static final String MSG_TYPE_VIDEO = "video";
    public static final String MSG_TYPE_IMAGE = "image";
    public static final String MSG_TYPE_EVENT = "event";
    public static final String MSG_REGX_TYPE_PART = "0";
    public static final String MSG_REGX_TYPE_ALL = "1";
    public static final String MSG_MENU_ID = "menu";


    public static final String GET_ACCESS_TOKEN = "https://xx/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";

    public static final String APP_GET_ACCESS_TOKEN_URL = "https://xx/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static final String APP_GET_USER_INFO_URL = "https://xxm/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    public static final String OPEN_AUTH = "https://xx/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URL&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    //创建菜单
    public static final String CREATE_MENU = "https://xx/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //支付适配器
    public static final String PAY_ADAPT = "PAY_ADAPT";
    //支付通知适配器
    public static final String PAY_NOTIFY_ADAPT = "PAY_NOTIFY_ADAPT";
}

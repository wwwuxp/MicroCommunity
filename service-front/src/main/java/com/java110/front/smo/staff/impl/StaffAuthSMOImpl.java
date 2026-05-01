package com.java110.front.smo.staff.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.smo.front.AbstractFrontServiceSMO;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.core.factory.AuthenticationFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.smo.IGetCommunityStoreInfoSMO;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import com.java110.dto.user.UserDto;
import com.java110.front.properties.WechatAuthProperties;
import com.java110.front.smo.staff.IStaffAuthSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.utils.cache.CommonCache;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * wx登录
 */
@Service("staffAuthSMOImpl")
public class StaffAuthSMOImpl extends AbstractFrontServiceSMO implements IStaffAuthSMO {

    private final static Logger logger = LoggerFactory.getLogger(StaffAuthSMOImpl.class);

    private final static int expireTime = 7200;

    private final static int LOGIN_PAGE = 1;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired(required = false)
    private IGetCommunityStoreInfoSMO getCommunityStoreInfoSMOImpl;

    @Autowired
    private Executor asyncServiceExecutor;

    @Override
    public ResponseEntity<String> getPageAccessToken(IPageData pd, HttpServletRequest request) throws SMOException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String authCode = paramIn.getString("code");
        String staffId = paramIn.getString("staffId");
        String storeId = paramIn.getString("storeId");
        String wId = paramIn.getString("wId");
        SmallWeChatDto smallWeChatDto = null;
        if (!StringUtil.isEmpty(wId)) {
            paramIn.put("appId", WechatFactory.getAppId(wId));
            smallWeChatDto = getSmallWechat(pd, paramIn);
        }
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        String url = WechatConstant.APP_GET_ACCESS_TOKEN_URL.replace("APPID", smallWeChatDto.getAppId())
                .replace("SECRET", smallWeChatDto.getAppSecret())
                .replace("CODE", authCode);

        ResponseEntity<String> paramOut = outRestTemplate.getForEntity(url, String.class);

        logger.debug("调用微信换去openId ", paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage("/");

        }

        JSONObject paramObj = JSONObject.parseObject(paramOut.getBody());
        //获取 openId
        String openId = paramObj.getString("openid");
        url = WechatConstant.APP_GET_USER_INFO_URL
                .replace("ACCESS_TOKEN", paramObj.getString("access_token"))
                .replace("OPENID", openId);

        paramOut = outRestTemplate.getForEntity(url, String.class);

        logger.debug("调用微信换去openId ", paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            return ResultVo.redirectPage("/");
        }
        paramObj = JSONObject.parseObject(paramOut.getBody());
        JSONObject paramAuth = new JSONObject();
        paramAuth.put("openId", openId);
        paramAuth.put("staffId", staffId);
        paramAuth.put("storeId", storeId);
        paramAuth.put("appType", "WECHAT");
        paramAuth.put("state", "2002");
        paramAuth.put("openName", paramObj.getString("nickname"));

        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, paramAuth.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/staff/updateStaffAppAuth", HttpMethod.POST);
        url = MappingCache.getValue("OWNER_WECHAT_URL");
        return ResultVo.redirectPage(url);

    }

    /**
     * 刷新token
     *
     * @param pd
     * @param request
     * @param response
     * @return
     * @throws SMOException
     */
    @Override
    public ResponseEntity<String> refreshToken(IPageData pd, String communityId, String staffId, String storeId,
                                               HttpServletRequest request, HttpServletResponse response) throws SMOException {
        SmallWeChatDto smallWeChatDto = null;
        JSONObject paramIn = new JSONObject();
        paramIn.put("communityId", communityId);
        smallWeChatDto = getSmallWechat(pd, paramIn);
        if (smallWeChatDto == null) { //从配置文件中获取 小程序配置信息
            smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setAppId(wechatAuthProperties.getWechatAppId());
            smallWeChatDto.setAppSecret(wechatAuthProperties.getWechatAppSecret());
            smallWeChatDto.setMchId(wechatAuthProperties.getMchId());
            smallWeChatDto.setPayPassword(wechatAuthProperties.getKey());
        }

        String openUrl = "";
        String url = MappingCache.getValue("OWNER_WECHAT_URL");
        try {
            openUrl = WechatConstant.OPEN_AUTH
                    .replace("APPID", smallWeChatDto.getAppId())
                    //.replace("SCOPE", "snsapi_base")
                    .replace("SCOPE", "snsapi_userinfo")
                    .replace(
                            "REDIRECT_URL",
                            URLEncoder
                                    .encode(
                                            (url
                                                    + "/app/loginStaffWechatAuth?appId=992020061452450002&staffId="
                                                    + staffId + "&storeId=" + storeId + "&wId=" + WechatFactory.getWId(smallWeChatDto.getAppId()))
                                                    + "&communityId=" + communityId,
                                            "UTF-8")).replace("STATE", "1");
            response.sendRedirect(openUrl);
        } catch (Exception e) {
            logger.error("微信公众号鉴权 redirectUrl 错误 " + url, e);
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, e.getLocalizedMessage());
        }

        JSONObject urlObj = new JSONObject();
        urlObj.put("openUrl", openUrl);

        return ResultVo.createResponseEntity(ResultVo.CODE_MACHINE_OK, ResultVo.MSG_OK, urlObj);
    }

    @Override
    public ResponseEntity<String> getStaffH5AuthUrl(IPageData pd, HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = wechatAuthProperties.getRedirectUrl();

        String authUrl;
        try {
            authUrl = WechatConstant.QY_WEB_AUTH.replace("CORPID", wechatAuthProperties.getCorpId())
                    .replace("REDIRECT_URI", URLEncoder.encode(redirectUrl, "UTF-8"))
                    .replace("AGENTID", wechatAuthProperties.getAgentId())
                    .replace("STATE", "1");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("移动端网页认证失败");
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, e.getLocalizedMessage());
        }

        JSONObject urlJson = new JSONObject();
        urlJson.put("authUrl", authUrl);

        return ResultVo.createResponseEntity(urlJson);
    }

    @Override
    public ResponseEntity<String> staffH5Auth(IPageData pd, HttpServletRequest request) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        String authCode = paramIn.getString("code");
        String state = paramIn.getString("state");

        // 需要缓存access_token，用于后续接口的调用（注意：不能频繁调用gettoken接口，否则会受到频率拦截）。当access_token失效或过期时，需要重新获取
        String accessToken = CommonCache.getValue(CommonConstant.QYWX_ACCESS_TOKEN + wechatAuthProperties.getCorpId());
        if (StringUtils.isBlank(accessToken)){
            // 调用微信接口获取access_token
            String gerAccessTokenUrl = WechatConstant.GET_QY_ACCESS_TOKEN.replace("ID", wechatAuthProperties.getCorpId())
                    .replace("SECRET", wechatAuthProperties.getCorpSecret());
            logger.info("gerAccessTokenUrl 是：{}", gerAccessTokenUrl);
            ResponseEntity<String> accessTokenParamOut = outRestTemplate.getForEntity(gerAccessTokenUrl, String.class);
            if (accessTokenParamOut.getStatusCode() != HttpStatus.OK){
                return ResultVo.redirectPage("/");
            }
            JSONObject accessTokenJson = JSONObject.parseObject(accessTokenParamOut.getBody());
            // 获取到accessToken
            accessToken = accessTokenJson.getString("access_token");
            int expireTime = accessTokenJson.getIntValue("expires_in");
            if (StringUtils.isNotBlank(accessToken)){
                CommonCache.setValue(CommonConstant.QYWX_ACCESS_TOKEN + wechatAuthProperties.getCorpId(), accessToken, expireTime);
            }
        }
        logger.info("获取到access_token:{}", accessToken);


        // 调用微信接口获取用户身份
        String getUserIdUrl = WechatConstant.GET_QY_USER_ID.replace("ACCESS_TOKEN", accessToken).replace("CODE", authCode);

        logger.info("getUserInfoUrl 是：{}", getUserIdUrl);
        ResponseEntity<String> userIdParamOut = outRestTemplate.getForEntity(getUserIdUrl, String.class);
        if (userIdParamOut.getStatusCode() != HttpStatus.OK){
            return ResultVo.redirectPage("/");
        }
        JSONObject userIdJson = JSONObject.parseObject(userIdParamOut.getBody());
        logger.info("userIdJson是：{}", userIdJson);
        // 获取到userId
        String userId = userIdJson.getString("UserId");
        logger.info("获取到的userId是：{}", userId);
        if (StringUtils.isBlank(userId)){
            // 非企业成员
            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "未获取到用户信息，请关闭页面重新进入");
        }

        // 根据useId从数据库中获取用户信息
        List<UserDto> userInfoList = userInnerServiceSMOImpl.getUserInfo(new String[]{userId});
        logger.info("获取到的userInfoList是：{}", userInfoList);
        if (CollectionUtils.isNotEmpty(userInfoList)){
            if (userInfoList.size() > 1){
                throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "发现多名用户");
            }
        }else {
            // 库中没有该用户的记录
            String isSyncWxData = CommonCache.getValue(CommonConstant.QYWX_SYNC_DATA);
            if (StringUtils.isEmpty(isSyncWxData)){
                CompletableFuture<Void> async = CompletableFuture.runAsync(() -> {
                    CommonCache.setValue(CommonConstant.QYWX_SYNC_DATA, "1", CommonCache.defaultExpireTime * 6);
                    // 设置过期时间30分钟，避免远程请求错误，导致该key一直存在
                    outRestTemplate.getForEntity(CommonConstant.QYWX_SYNC_URL, String.class);
                    CommonCache.removeValue(CommonConstant.QYWX_SYNC_DATA);
                }, asyncServiceExecutor);
                async.join();
            }

            throw new SMOException(ResponseConstant.RESULT_CODE_ERROR, "您的账号不在本系统中，账号数据每天凌晨同步一次");
        }

        pd = PageData.newInstance().builder(userId, "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        ResultVo resultVo = getCommunityStoreInfoSMOImpl.getStoreInfo(pd, restTemplate, pd.getUserId());
        JSONObject storeInfo = JSONObject.parseObject(resultVo.getMsg());
        logger.info("获取到的storeInfo是：{}", storeInfo.toJSONString());
        Assert.jsonObjectHaveKey(storeInfo, "storeId", "未找到该员工对应的组织信息");
        Assert.jsonObjectHaveKey(storeInfo, "storeTypeCd", "未找到该员工对应的组织类型信息");
        try {
            UserDto userDto = userInfoList.get(0);
            JSONObject userInfo = new JSONObject();
            userInfo.put("userId", userId);
            userInfo.put("userName", userDto.getUserName());
            userInfo.put("tel", userDto.getTel());

            Map<String, String> userMap = new HashMap<>();
            userMap.put(CommonConstant.LOGIN_USER_ID, userId);
            userMap.put(CommonConstant.LOGIN_USER_NAME, userDto.getUserName());
            String token = AuthenticationFactory.createAndSaveToken(userMap);

            userInfo.put("token", token);
            userInfo.put("storeId", storeInfo.getString("storeId"));
            userInfo.put("storeName", storeInfo.getString("name"));
            userInfo.put("storeTypeCd", storeInfo.getString("storeTypeCd"));

            JSONObject paramOut = new JSONObject();
            paramOut.put("result", 0);
            paramOut.put("userInfo", userInfo);
            paramOut.put("token", token);

            return new ResponseEntity<>(paramOut.toJSONString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "系统内部错误，请联系管理员");
        }

    }

    /**
     * 判断 绑定表里是否存在当前 端 绑定信息
     *
     * @param ownerAppUserDtos
     * @param appType
     * @return
     */
    private OwnerAppUserDto judgeCurrentOwnerBind(List<OwnerAppUserDto> ownerAppUserDtos, String appType) {

        for (OwnerAppUserDto ownerAppUserDto : ownerAppUserDtos) {
            if (appType.equals(ownerAppUserDto.getAppType())) {
                return ownerAppUserDto;
            }
        }
        return null;
    }

    private SmallWeChatDto getSmallWechat(IPageData pd, JSONObject paramIn) {

        ResponseEntity responseEntity = null;

        pd = PageData.newInstance().builder(pd.getUserId(), "", "", pd.getReqData(),
                "", "", "", "",
                pd.getAppId());
        responseEntity = this.callCenterService(restTemplate, pd, "",
                ServiceConstant.SERVICE_API_URL + "/api/smallWeChat.listSmallWeChats?communityId="
                        + paramIn.getString("communityId") + "&page=1&row=1&weChatType=" + SmallWeChatDto.WECHAT_TYPE_PUBLIC, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");

        if (smallWeChats == null || smallWeChats.size() < 1) {
            return null;
        }

        return BeanConvertUtil.covertBean(smallWeChats.get(0), SmallWeChatDto.class);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}

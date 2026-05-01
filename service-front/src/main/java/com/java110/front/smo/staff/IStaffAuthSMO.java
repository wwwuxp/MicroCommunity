package com.java110.front.smo.staff;

import com.java110.core.context.IPageData;
import com.java110.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 组织管理管理服务接口类
 * <p>
 * add by wuxw 2019-06-29
 */
public interface IStaffAuthSMO {



    /**
     * 获取access_token
     *
     * @param pd
     * @return
     * @throws SMOException
     */
    ResponseEntity<String> getPageAccessToken(IPageData pd,HttpServletRequest request) throws SMOException;


    /**
     * 微信刷新token
     *
     * @param pd
     * @return
     * @throws SMOException
     */
    ResponseEntity<String> refreshToken(IPageData pd,String communityId,String staffId,String storeId,
                                        HttpServletRequest request, HttpServletResponse response) throws SMOException;

    /**
     * new add
     * 获取员工h5授权地址
     * @param pd 封装数据
     * @param request 请求
     * @param response 响应
     * @return
     */
    ResponseEntity<String> getStaffH5AuthUrl(IPageData pd, HttpServletRequest request, HttpServletResponse response);

    /**
     * new add
     * 员工h5授权
     * @param pd 封装数据
     * @param request 请求
     * @return responseEntity
     */
    ResponseEntity<String> staffH5Auth(IPageData pd, HttpServletRequest request);

}

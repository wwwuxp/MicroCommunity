package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarUser.DepartmentStatisticsDto;
import com.java110.dto.pubCarUser.PubCarExistUserDto;
import com.java110.dto.pubCarUser.PubCarUserDto;
import com.java110.dto.user.UserSimpleDto;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarUser.ApiDepartmentNamesDataVo;
import com.java110.vo.api.pubCarUser.ApiDepartmentStatisticsDataVo;
import com.java110.vo.api.pubCarUser.ApiPubCarUserDetailDataVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用车人管理表组件内部之间使用，没有给外围系统提供服务能力
 * 用车人管理表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarUserServiceDao {

    /**
     * 保存 用车人管理表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarUserInfo(Map info) throws DAOException;

    /**
     * 查询用车人管理表信息（instance过程）
     * 根据bId 查询用车人管理表信息
     * @param info bId 信息
     * @return 用车人管理表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarUserInfo(Map info) throws DAOException;

    /**
     * 修改用车人管理表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarUserInfo(Map info) throws DAOException;

    /**
     * 查询用车人管理表总数
     *
     * @param info 用车人管理表信息
     * @return 用车人管理表数量
     */
    int queryPubCarUsersCount(Map info);

    void saveBusinessPubCarUserInfo(JSONObject businessPubCarUser);

    List<Map> getBusinessPubCarUserInfo(Map info);

    List<Map> getPubCarUserInfos(Map beanCovertMap);

    int queryPubCarUserDetailsCount(PubCarUserDto pubCarUserDto);

    List<ApiPubCarUserDetailDataVo> getPubCarUserDetails(PubCarUserDto pubCarUserDto);

    int queryDepartmentStatisticsCount(DepartmentStatisticsDto departmentStatisticsDto);

    List<ApiDepartmentStatisticsDataVo> queryDepartmentStatistics(DepartmentStatisticsDto departmentStatisticsDto);

    List<ApiDepartmentNamesDataVo> queryDepartmentNames(String storeId);

    List<String> listExistPubCarUser(PubCarExistUserDto existUserDto);

    /**
     * 根据id批量获取姓名，部门，职位信息
     * @param userIds id集合
     * @return list
     */
    List<UserSimpleDto> getNameDepartmentPositionInfoBatch(List<String> userIds);

    /**
     * 获取用户姓名
     * @param userIdSet 参数
     * @return List
     */
    List<Map<String, String>> getUserNameByUserIds(Set<String> userIdSet);

}

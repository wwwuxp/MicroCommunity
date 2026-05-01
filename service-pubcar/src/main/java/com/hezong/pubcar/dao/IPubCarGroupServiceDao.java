package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarGroup.PubCarGroupDto;
import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;
import com.java110.vo.api.pubCarGroup.ApiPubCarGroupDataVo;

import java.util.List;
import java.util.Map;

/**
 * 分组信息表组件内部之间使用，没有给外围系统提供服务能力
 * 分组信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarGroupServiceDao {

    /**
     * 保存 分组信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarGroupInfo(Map info) throws DAOException;

    /**
     * 查询分组信息表信息（instance过程）
     * 根据bId 查询分组信息表信息
     * @param info bId 信息
     * @return 分组信息表信息
     * @throws DAOException DAO异常
     */
    List<Map> getPubCarGroupInfo(Map info) throws DAOException;

    /**
     * 修改分组信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarGroupInfo(Map info) throws DAOException;

    /**
     * 查询分组信息表总数
     * @return 分组信息表数量
     */
    int queryPubCarGroupsCount(PubCarGroupDto pubCarGroupDto);

    void saveBusinessPubCarGroupInfo(JSONObject businessPubCarGroup);

    List<Map> getBusinessPubCarGroupInfo(Map info);

    List<ApiPubCarGroupDataVo> queryPubCarGroupNames(String communityId);

    Integer existPubCarGroup(PubCarGroupDto existGroupDto);
}

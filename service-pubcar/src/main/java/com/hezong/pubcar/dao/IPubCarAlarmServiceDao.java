package com.hezong.pubcar.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.pubCarAlarm.PubCarAlarmDto;
import com.java110.po.pubCarAlarm.PubCarAlarmPo;
import com.java110.utils.exception.DAOException;
import com.java110.vo.api.pubCarAlarm.ApiPubCarAlarmDataVo;

import java.util.List;
import java.util.Map;

/**
 * 异常报警信息表组件内部之间使用，没有给外围系统提供服务能力
 * 异常报警信息表服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by hh
 */
public interface IPubCarAlarmServiceDao {

    /**
     * 保存 异常报警信息表信息
     * @param info
     * @throws DAOException DAO异常
     */
    void savePubCarAlarmInfo(Map info) throws DAOException;

    /**
     * 查询异常报警信息表信息（instance过程）
     * 根据bId 查询异常报警信息表信息
     * @param info bId 信息
     * @return 异常报警信息表信息
     * @throws DAOException DAO异常
     */
    List<ApiPubCarAlarmDataVo> getPubCarAlarmInfo(PubCarAlarmDto info) throws DAOException;

    /**
     * 修改异常报警信息表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updatePubCarAlarmInfo(Map info) throws DAOException;

    /**
     * 查询异常报警信息表总数
     *
     * @param info 异常报警信息表信息
     * @return 异常报警信息表数量
     */
    int queryPubCarAlarmsCount(PubCarAlarmDto info);

    void saveBusinessPubCarAlarmInfo(JSONObject businessPubCarAlarm);

    List<Map> getBusinessPubCarAlarmInfo(Map info);

    List<Map> getPubCarAlarmInstanceInfo(Map<String, String> info);

    /**
     * 批量保存用车报警信息
     * @param pubCarAlarmPos 数据
     */
    void savePubCarAlarmBatch(List<PubCarAlarmPo> pubCarAlarmPos);

}

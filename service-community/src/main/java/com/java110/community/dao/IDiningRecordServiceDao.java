package com.java110.community.dao;

import com.java110.dto.diningRecord.DiningRecordDto;
import com.java110.vo.api.diningRecord.ApiDiningDetailDataVo;
import com.java110.vo.api.diningRecord.ApiDiningRecordDataVo;

import java.util.List;

/**
 * @author hh
 * @date 2021/11/11
 */
public interface IDiningRecordServiceDao {

    /**
     * 查询就餐记录统计总数量
     * @param diningRecordDto 请求参数
     * @return int
     */
    int queryDiningRecordStatisticsCount(DiningRecordDto diningRecordDto);

    /**
     * 查询就餐记录统计
     * @param diningRecordDto 请求参数
     * @return list
     */
    List<ApiDiningRecordDataVo> queryDiningRecordStatistics(DiningRecordDto diningRecordDto);

    /**
     * 获取员工就餐详情
     * @param diningRecordDto 请求参数
     * @return list
     */
    List<ApiDiningDetailDataVo> queryDiningDetails(DiningRecordDto diningRecordDto);

}

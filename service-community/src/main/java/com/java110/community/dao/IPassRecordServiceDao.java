package com.java110.community.dao;

import com.java110.dto.attendance.PassRecordDto;
import com.java110.vo.api.passRecord.ApiPassRecordDataVo;

import java.util.List;

/**
 * @author hh
 * @date 2022/1/26
 */
public interface IPassRecordServiceDao {

    /**
     * 查询总的通行记录数量
     * @param passRecordDto 参数
     * @return int
     */
    int queryPassRecordsCount(PassRecordDto passRecordDto);

    /**
     * 查询通行记录列表数据
     * @param passRecordDto 参数
     * @return List
     */
    List<ApiPassRecordDataVo> queryPassRecords(PassRecordDto passRecordDto);

}

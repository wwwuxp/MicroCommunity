package com.java110.community.dao.impl;

import com.java110.community.dao.IDiningRecordServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.diningRecord.DiningRecordDto;
import com.java110.vo.api.diningRecord.ApiDiningDetailDataVo;
import com.java110.vo.api.diningRecord.ApiDiningRecordDataVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hh
 * @date 2021/11/11
 */
@Service("diningRecordServiceDaoImpl")
public class DiningRecordServiceDaoImpl extends BaseServiceDao implements IDiningRecordServiceDao {

    @Override
    public int queryDiningRecordStatisticsCount(DiningRecordDto diningRecordDto) {
        return sqlSessionTemplate.selectOne("diningRecordServiceDaoImpl.queryDiningRecordStatisticsCount", diningRecordDto);
    }

    @Override
    public List<ApiDiningRecordDataVo> queryDiningRecordStatistics(DiningRecordDto diningRecordDto) {
        return sqlSessionTemplate.selectList("diningRecordServiceDaoImpl.queryDiningRecordStatistics", diningRecordDto);
    }

    @Override
    public List<ApiDiningDetailDataVo> queryDiningDetails(DiningRecordDto diningRecordDto) {
        return sqlSessionTemplate.selectList("diningRecordServiceDaoImpl.queryDiningDetails", diningRecordDto);
    }
}

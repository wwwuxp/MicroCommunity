package com.java110.community.dao.impl;

import com.java110.community.dao.IPassRecordServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.attendance.PassRecordDto;
import com.java110.vo.api.passRecord.ApiPassRecordDataVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hh
 * @date 2022/1/26
 */
@Service("passRecordServiceDaoImpl")
public class PassRecordServiceDaoImpl extends BaseServiceDao implements IPassRecordServiceDao {


    @Override
    public int queryPassRecordsCount(PassRecordDto passRecordDto) {
        return sqlSessionTemplate.selectOne("passRecordServiceDaoImpl.queryPassRecordsCount", passRecordDto);
    }

    @Override
    public List<ApiPassRecordDataVo> queryPassRecords(PassRecordDto passRecordDto) {
        return sqlSessionTemplate.selectList("passRecordServiceDaoImpl.queryPassRecords", passRecordDto);
    }

}

package com.java110.community.smo.impl;

import com.java110.community.dao.IPassRecordServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.PassRecordDto;
import com.java110.intf.community.IPassRecordInnerServiceSMO;
import com.java110.vo.api.passRecord.ApiPassRecordDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hh
 * @date 2022/1/26
 */
@RestController
public class PassRecordInnerServiceSMOImpl extends BaseServiceSMO implements IPassRecordInnerServiceSMO {

    @Autowired
    private IPassRecordServiceDao passRecordServiceDaoImpl;

    @Override
    public int queryPassRecordsCount(@RequestBody PassRecordDto passRecordDto) {
        return passRecordServiceDaoImpl.queryPassRecordsCount(passRecordDto);
    }

    @Override
    public List<ApiPassRecordDataVo> queryPassRecords(@RequestBody PassRecordDto passRecordDto) {
        //校验是否传了 分页信息
        int page = passRecordDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            passRecordDto.setPage((page - 1) * passRecordDto.getRow());
        }
        return passRecordServiceDaoImpl.queryPassRecords(passRecordDto);
    }
}

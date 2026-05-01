package com.hezong.pubcar.smo.impl;

import com.hezong.pubcar.dao.IPubCarReimburseServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.pubCarReimburse.PubCarReimburseDto;
import com.java110.intf.pubcar.IPubCarReimburseInnerServiceSMO;
import com.java110.vo.api.pubCarReimburse.ApiPubCarReimburseDataVo;
import com.java110.vo.api.pubCarReimburse.ReimburseDetailInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author hh
 * @date 2021/10/12
 */
@RestController
public class PubCarReimburseInnerServiceSMOImpl extends BaseServiceSMO implements IPubCarReimburseInnerServiceSMO {

    @Autowired
    private IPubCarReimburseServiceDao pubCarReimburseServiceDaoImpl;

    @Override
    public int listPubCarReimbursesCount(@RequestBody PubCarReimburseDto pubCarReimburseDto) {
        return pubCarReimburseServiceDaoImpl.listPubCarReimbursesCount(pubCarReimburseDto);
    }

    @Override
    public List<ApiPubCarReimburseDataVo> listPubCarReimburses(@RequestBody PubCarReimburseDto pubCarReimburseDto) {
        //校验是否传了 分页信息
        int page = pubCarReimburseDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            pubCarReimburseDto.setPage((page - 1) * pubCarReimburseDto.getRow());
        }
        // 格式化报销月份
        LocalDate carUseDate = LocalDate.parse(pubCarReimburseDto.getLessCarUseDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String reimburseAmount = carUseDate.format(DateTimeFormatter.ofPattern("yyyy年MM月"));

        List<ApiPubCarReimburseDataVo> pubCarReimburseDataVos = pubCarReimburseServiceDaoImpl.listPubCarReimburses(pubCarReimburseDto);
        for (ApiPubCarReimburseDataVo pubCarReimburseDataVo : pubCarReimburseDataVos) {
            // 设置报销月份
            pubCarReimburseDataVo.setReimburseMonth(reimburseAmount);

            BigDecimal totalAmount = new BigDecimal("0.00");
            List<ReimburseDetailInfo> reimburses = pubCarReimburseDataVo.getReimburses();
            for (ReimburseDetailInfo reimburse : reimburses) {
                totalAmount = totalAmount.add(reimburse.getReimburseAmount());

                // 处理电子发票
                String[] invoiceArray = new String[0];
                String invoice = reimburse.getInvoice();
                if (StringUtils.isNotBlank(invoice)){
                    invoiceArray = invoice.split(",");
                }
                reimburse.setInvoices(invoiceArray);
                reimburse.setInvoice(null);
            }
            pubCarReimburseDataVo.setTotalAmount(totalAmount);
            pubCarReimburseDataVo.setUserId(null);

        }
        return pubCarReimburseDataVos;
    }

    @Override
    public boolean auditPubCarReimburses(@RequestBody PubCarReimburseDto pubCarReimburseDto) {
        int auditFlag = pubCarReimburseServiceDaoImpl.auditPubCarReimburses(pubCarReimburseDto);
        return auditFlag >= 1;
    }
}

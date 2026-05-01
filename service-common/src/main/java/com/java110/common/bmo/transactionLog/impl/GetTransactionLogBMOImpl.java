package com.java110.common.bmo.transactionLog.impl;

import com.java110.common.bmo.transactionLog.IGetTransactionLogBMO;
import com.java110.dto.transactionLog.TransactionLogDto;
import com.java110.intf.common.ITransactionLogInnerServiceSMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("getTransactionLogBMOImpl")
public class GetTransactionLogBMOImpl implements IGetTransactionLogBMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    /**
     * @param transactionLogDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(TransactionLogDto transactionLogDto) {


        int count = transactionLogInnerServiceSMOImpl.queryTransactionLogsCount(transactionLogDto);

        List<TransactionLogDto> transactionLogDtos = null;
        if (count > 0) {
            transactionLogDtos = handleCostTime(transactionLogInnerServiceSMOImpl.queryTransactionLogs(transactionLogDto));
        } else {
            transactionLogDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) transactionLogDto.getRow()), count, transactionLogDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * new add 处理耗时，时间携带单位
     * @param transactionLogs
     * @return
     */
    private List<TransactionLogDto> handleCostTime(List<TransactionLogDto> transactionLogs) {
        return transactionLogs.stream().peek(t -> {
            long costTime = Long.parseLong(t.getCostTime());
            String costTimeStr = null;
            if (costTime < 1000){
                costTimeStr = costTime + "毫秒";
            }else if (costTime < 60000){
                costTimeStr = String.format("%.1f", costTime / 1000.0) + "秒";
            }else if (costTime < 3600000){
                costTimeStr = String.format("%.1f", costTime / 60000.0) + "分";
            }else if (costTime < 86400000){
                costTimeStr = String.format("%.1f", costTime / 3600000.0) + "时";
            }
            t.setCostTime(costTimeStr);
        }).collect(Collectors.toList());
    }

}

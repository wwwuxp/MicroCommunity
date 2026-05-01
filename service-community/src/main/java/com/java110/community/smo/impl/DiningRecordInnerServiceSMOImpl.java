package com.java110.community.smo.impl;

import com.java110.community.dao.IDiningRecordServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.diningRecord.DiningRecordDto;
import com.java110.intf.community.IDiningRecordInnerServiceSMO;
import com.java110.utils.util.ConstantUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.diningRecord.ApiCountTotalDiningRecordVo;
import com.java110.vo.api.diningRecord.ApiDiningDetailDataVo;
import com.java110.vo.api.diningRecord.ApiDiningRecordDataVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

/**
 * 就餐记录内部服务类
 * @author hh
 * @date 2021/11/11
 */
@RestController
public class DiningRecordInnerServiceSMOImpl extends BaseServiceSMO implements IDiningRecordInnerServiceSMO {

    @Autowired
    private IDiningRecordServiceDao diningRecordServiceDaoImpl;

    @Override
    public int queryDiningRecordStatisticsCount(@RequestBody DiningRecordDto diningRecordDto) {
        return diningRecordServiceDaoImpl.queryDiningRecordStatisticsCount(diningRecordDto);
    }

    @Override
    public List<ApiDiningRecordDataVo> queryDiningRecordStatistics(@RequestBody DiningRecordDto diningRecordDto) {
        String startDate = diningRecordDto.getStartDate();
        String endDate = diningRecordDto.getEndDate();
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
            throw new IllegalArgumentException("时间不能为空");
        }

        // 检查分页信息
        int page = diningRecordDto.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            diningRecordDto.setPage((page - 1) * diningRecordDto.getRow());
        }
        List<ApiDiningRecordDataVo> countDiningList = diningRecordServiceDaoImpl.queryDiningRecordStatistics(diningRecordDto);
        // if (endDate.endsWith("31")){
        //     // 类如：9月，传递进来了2021-09-31，经过下面一步会自动转为2021-09-30
        //     LocalDate endDateParse = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        //     endDate = endDateParse.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        // }
        for (ApiDiningRecordDataVo apiDiningRecordDataVo : countDiningList) {
            apiDiningRecordDataVo.setTotal(apiDiningRecordDataVo.getMorning() + apiDiningRecordDataVo.getNoon() + apiDiningRecordDataVo.getAfternoon());
        }
        return countDiningList;
    }

    @Override
    public List<ApiDiningDetailDataVo> queryDiningDetails(@RequestBody DiningRecordDto diningRecordDto) {
        List<ApiDiningDetailDataVo> diningDetailDataVos = diningRecordServiceDaoImpl.queryDiningDetails(diningRecordDto);
        for (ApiDiningDetailDataVo diningDetailDataVo : diningDetailDataVos) {
            // 数据可能是这样子（07:30:00,10:30:00）
            String diningTime = diningDetailDataVo.getDiningTime();
            String[] diningTimeSplit = diningTime.split(",");

            StringJoiner morningSj = new StringJoiner("、");
            StringJoiner noonSj = new StringJoiner("、");
            StringJoiner afternoonSj = new StringJoiner("、");
            for (String each : diningTimeSplit) {
                LocalTime parse = LocalTime.parse(each, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
                // 早上就餐
                boolean isMorning = (parse.isAfter(ConstantUtil.MORNING_START_TIME) || parse.equals(ConstantUtil.MORNING_START_TIME))
                        && (parse.isBefore(ConstantUtil.MORNING_END_TIME) || parse.equals(ConstantUtil.MORNING_END_TIME));
                if (isMorning){
                    morningSj.add(parse.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_N)));
                }
                // 中午就餐
                boolean isNoon = (parse.isAfter(ConstantUtil.NOON_START_TIME) || parse.equals(ConstantUtil.NOON_START_TIME))
                        && (parse.isBefore(ConstantUtil.NOON_END_TIME) || parse.equals(ConstantUtil.NOON_END_TIME));
                if (isNoon){
                    noonSj.add(parse.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_N)));
                }
                // 晚上就餐
                boolean isAfternoon = (parse.isAfter(ConstantUtil.AFTERNOON_START_TIME) || parse.equals(ConstantUtil.AFTERNOON_START_TIME))
                        && (parse.isBefore(ConstantUtil.AFTERNOON_END_TIME) || parse.equals(ConstantUtil.AFTERNOON_END_TIME));
                if (isAfternoon){
                    afternoonSj.add(parse.format(DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_N)));
                }
            }

            // 填充数据
            diningDetailDataVo.setMorningDiningTime(morningSj.toString());
            diningDetailDataVo.setNoonDiningTime(noonSj.toString());
            diningDetailDataVo.setAfternoonDiningTime(afternoonSj.toString());

            // 计算每天的用餐数
            int count = 0;
            if (StringUtils.isNotBlank(diningDetailDataVo.getMorningDiningTime())){
                count++;
            }
            if (StringUtils.isNotBlank(diningDetailDataVo.getNoonDiningTime())){
                count++;
            }
            if (StringUtils.isNotBlank(diningDetailDataVo.getAfternoonDiningTime())){
                count++;
            }
            diningDetailDataVo.setDiningCount(count);

        }

        return diningDetailDataVos;
    }

    @Override
    public ApiCountTotalDiningRecordVo queryTotalDiningRecordStatisticsCount(@RequestBody DiningRecordDto diningRecordDto) {
        int page = 1;
        int row = 500;
        int count = diningRecordServiceDaoImpl.queryDiningRecordStatisticsCount(diningRecordDto);
        int totalPage = (int) Math.ceil((double) count / (double) row);
        diningRecordDto.setRow(row);
        int total;
        int morning = 0;
        int noon = 0;
        int afternoon = 0;
        if (count > 0){
            do {
                diningRecordDto.setPage((page - 1) * row);
                List<ApiDiningRecordDataVo> diningRecordDataVos = diningRecordServiceDaoImpl.queryDiningRecordStatistics(diningRecordDto);
                for (ApiDiningRecordDataVo diningRecordDataVo : diningRecordDataVos) {
                    morning += diningRecordDataVo.getMorning();
                    noon += diningRecordDataVo.getNoon();
                    afternoon += diningRecordDataVo.getAfternoon();
                }
                page++;
            } while (page <= totalPage);
        }

        // 计算总数
        total = morning + noon + afternoon;

        ApiCountTotalDiningRecordVo apiCountTotalDiningRecordVo = new ApiCountTotalDiningRecordVo();
        apiCountTotalDiningRecordVo.setTotal(total);
        apiCountTotalDiningRecordVo.setMorning(morning);
        apiCountTotalDiningRecordVo.setNoon(noon);
        apiCountTotalDiningRecordVo.setAfternoon(afternoon);
        return apiCountTotalDiningRecordVo;
    }

}

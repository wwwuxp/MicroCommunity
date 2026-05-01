package com.java110.front.smo.diningRecord.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.diningRecord.DiningRecordDto;
import com.java110.entity.diningRecord.DiningStatistics;
import com.java110.front.smo.diningRecord.IExportDiningStatisticsSMO;
import com.java110.intf.community.IDiningRecordInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.diningRecord.ApiDiningRecordDataVo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 导出就餐统计
 * @author hh
 * @date 2021/11/12
 */
@Service("exportDiningStatisticsSMOImpl")
public class ExportDiningStatisticsSMOImpl extends BaseComponentSMO implements IExportDiningStatisticsSMO {

    @Autowired
    private IDiningRecordInnerServiceSMO diningRecordInnerServiceSMOImpl;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) {
        JSONObject reqJson = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(reqJson, "startDate", "时间不能为空");
        Assert.hasKeyAndValue(reqJson, "endDate", "时间不能为空");
        DiningRecordDto diningRecordDto = BeanConvertUtil.covertBean(reqJson, DiningRecordDto.class);
        String startDateStr = reqJson.getString("startDate").trim();
        String endDateStr = reqJson.getString("endDate").trim();
        // 得到标准的日期格式,否则使用LocalDate解析出现错误
        String[] startDateArray = startDateStr.split("-");
        String startMonthValue = startDateArray[1];
        if (startMonthValue.length() == 1){
            startMonthValue = "0" + startMonthValue;
        }
        startDateStr = startDateArray[0] + "-" + startMonthValue + "-" + startDateArray[2];

        String[] endDateArray = endDateStr.split("-");
        String endMonthValue = endDateArray[1];
        if (endMonthValue.length() == 1){
            endMonthValue = "0" + endMonthValue;
        }
        endDateStr = endDateArray[0] + "-" + endMonthValue + "-" + endDateArray[2];

        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));

        String fileName = startDate.getYear() + "年" + startDate.getMonthValue() + "月" + startDate.getDayOfMonth() + "日到" +
                    endDate.getYear() + "年" + endDate.getMonthValue() + "月" + endDate.getDayOfMonth() + "日就餐统计数据";

        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        ByteArrayOutputStream os = null;
        byte[] context;
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        headers.add("Pargam", "no-cache");
        headers.add("Cache-Control", "no-cache");
        headers.add("Accept-Ranges", "bytes");

        try {
            //创建工作薄对象
            Workbook workbook=new XSSFWorkbook();
            //创建工作表对象
            Sheet sheet = workbook.createSheet("就餐统计");
            // 设置列宽
            sheet.setColumnWidth(0, 3000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 6500);
            sheet.setColumnWidth(3, 3000);
            sheet.setColumnWidth(4, 3000);
            sheet.setColumnWidth(5, 3000);
            sheet.setColumnWidth(6, 3000);

            //创建工作表的行, 第一行，从0开始
            Row row0 = sheet.createRow(0);
            row0.setHeight((short) 500);
            CellStyle titleStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            titleStyle.setFont(font);
            // 水平居中
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            // 垂直居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Cell cell0 = row0.createCell(0);
            cell0.setCellValue("姓名");
            cell0.setCellStyle(titleStyle);
            Cell cell1 = row0.createCell(1);
            cell1.setCellValue("部门");
            cell1.setCellStyle(titleStyle);
            Cell cell2 = row0.createCell(2);
            cell2.setCellValue("统计时间");
            cell2.setCellStyle(titleStyle);
            Cell cell3 = row0.createCell(3);
            cell3.setCellValue("上午就餐数");
            cell3.setCellStyle(titleStyle);
            Cell cell4 = row0.createCell(4);
            cell4.setCellValue("中午就餐数");
            cell4.setCellStyle(titleStyle);
            Cell cell5 = row0.createCell(5);
            cell5.setCellValue("晚上就餐数");
            cell5.setCellStyle(titleStyle);
            Cell cell6 = row0.createCell(6);
            cell6.setCellValue("总就餐数");
            cell6.setCellStyle(titleStyle);

            // 根据数据库中页码数决定写多少次
            int count = diningRecordInnerServiceSMOImpl.queryDiningRecordStatisticsCount(diningRecordDto);
            if (count <= 0){
                return new ResponseEntity<>("没有数据可以导出", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // 总页数
            int row = 500;
            int totalPage = (int) Math.ceil((double) count / (double) row);
            String countTime = startDate + "到" + endDate;
            int num = 0;
            for (int i = 1; i <= totalPage; i++) {
                diningRecordDto.setPage(i);
                diningRecordDto.setRow(row);
                List<ApiDiningRecordDataVo> diningRecordDataVos = diningRecordInnerServiceSMOImpl.queryDiningRecordStatistics(diningRecordDto);
                List<DiningStatistics> diningStatistics = BeanConvertUtil.covertBeanList(diningRecordDataVos, DiningStatistics.class);

                for (DiningStatistics diningStatistic : diningStatistics) {
                    num++;
                    Row dataRow = sheet.createRow(num);
                    dataRow.setHeight((short) 400);
                    Cell cell00 = dataRow.createCell(0);
                    cell00.setCellValue(diningStatistic.getName());
                    Cell cell11 = dataRow.createCell(1);
                    cell11.setCellValue(diningStatistic.getDepartment());
                    Cell cell22 = dataRow.createCell(2);
                    cell22.setCellValue(countTime);
                    Cell cell33 = dataRow.createCell(3);
                    cell33.setCellValue(diningStatistic.getMorning());
                    Cell cell44 = dataRow.createCell(4);
                    cell44.setCellValue(diningStatistic.getNoon());
                    Cell cell55 = dataRow.createCell(5);
                    cell55.setCellValue(diningStatistic.getAfternoon());
                    Cell cell66 = dataRow.createCell(6);
                    cell66.setCellValue(diningStatistic.getTotal());
                }
            }

            os = new ByteArrayOutputStream();
            workbook.write(os);
            context = os.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("导出失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResponseEntity<>(context, headers, HttpStatus.OK);
    }

}

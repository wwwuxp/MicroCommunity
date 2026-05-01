package com.java110.front.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.assetExport.IExportTempCarInoutFeeSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author hh
 * @datetime 2021/6/25 14:15
 */
@Service("exportTempCarInoutFeeSMOImpl")
public class ExportTempCarInoutFeeSMOImpl extends BaseComponentSMO implements IExportTempCarInoutFeeSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) {
        // todo
        ComponentValidateResult componentValidateResult = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "请求中未包含小区");

        // 获取临时停车费用信息
        JSONArray tempCarInoutFees = getTempCarInoutFees(pd, paramIn);
        if (tempCarInoutFees == null || tempCarInoutFees.size() < 1){
            return new ResponseEntity<>("没有需要导出的临时停车费用信息", HttpStatus.NO_CONTENT);
        }

        Workbook workbook = generateExcelData(tempCarInoutFees);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=tempCarInoutFeesInfo_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
        headers.add("Pargam", "no-cache");
        headers.add("Cache-Control", "no-cache");
        headers.add("Accept-Ranges", "bytes");

        byte[] context;
        try {
            workbook.write(bos);
            context = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("导出失败", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(context, headers, HttpStatus.OK);
    }

    private Workbook generateExcelData(JSONArray tempCarInoutFees) {
        // 工作表
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 工作sheet
        XSSFSheet sheet = workbook.createSheet("临时车缴费清单");
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell headCell = headRow.createCell(0);
        headCell.setCellValue("临时车缴费信息单");
        // 标题设置字体
        short defaultRowHeight = 300;
        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 26);
        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(font);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCell.setCellStyle(titleCellStyle);
        headRow.setHeight((short) (defaultRowHeight * 3));

        // 合并标题
        CellRangeAddress headRegion = new CellRangeAddress(0, 0, 0, 9);
        sheet.addMergedRegion(headRegion);

        // 通用样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置样式对象，这里仅设置了边框属性
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        // 设置标题
        int line = 1;
        Row titleRow = sheet.createRow(line);
        Cell cell0 = titleRow.createCell(0);
        cell0.setCellValue("订单ID");
        cell0.setCellStyle(cellStyle);
        Cell cell1 = titleRow.createCell(1);
        cell1.setCellValue("缴费ID");
        cell1.setCellStyle(cellStyle);
        Cell cell2 = titleRow.createCell(2);
        cell2.setCellValue("进场ID");
        cell2.setCellStyle(cellStyle);
        Cell cell3 = titleRow.createCell(3);
        cell3.setCellValue("车牌号");
        cell3.setCellStyle(cellStyle);
        Cell cell4 = titleRow.createCell(4);
        cell4.setCellValue("应付金额");
        cell4.setCellStyle(cellStyle);
        Cell cell5 = titleRow.createCell(5);
        cell5.setCellValue("实付金额");
        cell5.setCellStyle(cellStyle);
        Cell cell6 = titleRow.createCell(6);
        cell6.setCellValue("开始时间");
        cell6.setCellStyle(cellStyle);
        Cell cell7 = titleRow.createCell(7);
        cell7.setCellValue("结束时间");
        cell7.setCellStyle(cellStyle);
        Cell cell8 = titleRow.createCell(8);
        cell8.setCellValue("缴费时间");
        cell8.setCellStyle(cellStyle);

        BigDecimal totalReceivedAmount = new BigDecimal(0);
        for (int i = 0; i < tempCarInoutFees.size(); i++) {
            JSONObject tempCarInoutFeeObj = tempCarInoutFees.getJSONObject(i);
            // 创建一行
            Row row = sheet.createRow(line + i + 1);
            Cell dataCell0 = row.createCell(0);
            dataCell0.setCellValue(tempCarInoutFeeObj.getString("oId"));
            dataCell0.setCellStyle(cellStyle);
            Cell dataCell1 = row.createCell(1);
            dataCell1.setCellValue(tempCarInoutFeeObj.getString("feeId"));
            dataCell1.setCellStyle(cellStyle);
            Cell dataCell2 = row.createCell(2);
            dataCell2.setCellValue(tempCarInoutFeeObj.getString("inoutId"));
            dataCell2.setCellStyle(cellStyle);
            Cell dataCell3 = row.createCell(3);
            dataCell3.setCellValue(tempCarInoutFeeObj.getString("carNum"));
            dataCell3.setCellStyle(cellStyle);
            Cell dataCell4 = row.createCell(4);
            dataCell4.setCellValue(tempCarInoutFeeObj.getString("receivableAmount"));
            dataCell4.setCellStyle(cellStyle);
            Cell dataCell5 = row.createCell(5);
            dataCell5.setCellValue(tempCarInoutFeeObj.getString("receivedAmount"));
            dataCell5.setCellStyle(cellStyle);
            Cell dataCell6 = row.createCell(6);
            dataCell6.setCellValue(tempCarInoutFeeObj.getString("startTime"));
            dataCell6.setCellStyle(cellStyle);
            Cell dataCell7 = row.createCell(7);
            dataCell7.setCellValue(tempCarInoutFeeObj.getString("endTime"));
            dataCell7.setCellStyle(cellStyle);
            Cell dataCell8 = row.createCell(8);
            dataCell8.setCellValue(tempCarInoutFeeObj.getString("payTime"));
            dataCell8.setCellStyle(cellStyle);
            row.setHeight(defaultRowHeight);

            totalReceivedAmount = totalReceivedAmount.add(new BigDecimal(tempCarInoutFeeObj.getString("receivedAmount")));
        }

        Row totalAmountRow = sheet.createRow(line + tempCarInoutFees.size() + 1);
        Cell totalAmountCell0 = totalAmountRow.createCell(0);
        totalAmountCell0.setCellValue("实付总金额");
        totalAmountCell0.setCellStyle(cellStyle);
        Cell totalAmountCell1 = totalAmountRow.createCell(1);
        totalAmountCell1.setCellValue(totalReceivedAmount.doubleValue());
        totalAmountCell1.setCellStyle(cellStyle);
        totalAmountRow.setHeight(defaultRowHeight);

        return workbook;
    }

    private JSONArray getTempCarInoutFees(IPageData pd, JSONObject paramIn) {
        Integer page = paramIn.getInteger("page");
        Integer pageSize = paramIn.getInteger("row");
        paramIn.put("page", (page - 1) * pageSize );
        paramIn.put("row", page * pageSize);

        // 远程地址
        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/api.getTempCarInoutFee" + mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK){
            return null;
        }

        JSONObject tempCarInoutFeesResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        if (!tempCarInoutFeesResults.containsKey("tempCarInoutFees")){
            return null;
        }

        return tempCarInoutFeesResults.getJSONArray("tempCarInoutFees");
    }

}

package com.java110.front.smo.assetExport.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.front.smo.assetExport.IExportStaffFeeSMO;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @datetime 2021/6/24 10:37
 */
@Service("exportStaffFeeSMOImpl")
public class ExportStaffFeeSMOImpl extends BaseComponentSMO implements IExportStaffFeeSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<Object> exportExcelData(IPageData pd) {
        ComponentValidateResult componentValidateResult = this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        Assert.hasKeyAndValue(paramIn, "communityId", "请求中未包含小区");

        // 获取员工费用信息
        JSONArray staffFees = getStaffFees(pd, paramIn);
        if (staffFees == null || staffFees.size() < 1){
            return null;
        }

        Workbook workbook = generateExcelData(staffFees);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=staffFeesInfo_" + DateUtil.getyyyyMMddhhmmssDateString() + ".xlsx");
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

    private Workbook generateExcelData(JSONArray staffFees) {
        // 工作表
        Workbook workbook = new XSSFWorkbook();
        // 工作sheet
        Sheet sheet = workbook.createSheet("员工收费");
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("员工收费信息单");
        // 标题设置字体
        short defaultRowHeight = 300;
        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 26);
        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(font);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleCellStyle);
        titleRow.setHeight((short) (defaultRowHeight * 3));

        // 合并标题
        CellRangeAddress titleRegion = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(titleRegion);

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
        Row row = sheet.createRow(line);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("员工名称");
        cell0.setCellStyle(cellStyle);
        Cell cell1 = row.createCell(1);
        cell1.setCellValue("员工编码");
        cell1.setCellStyle(cellStyle);
        Cell cell2 = row.createCell(2);
        cell2.setCellValue("应收金额");
        cell2.setCellStyle(cellStyle);
        Cell cell3 = row.createCell(3);
        cell3.setCellValue("实收金额");
        cell3.setCellStyle(cellStyle);
        row.setHeight(defaultRowHeight);

        BigDecimal totalReceivedAmount = new BigDecimal(0);
        for (int i = 0; i < staffFees.size(); i++) {
            JSONObject staffFeeObj = staffFees.getJSONObject(i);
            // 创建一行
            Row dataRow = sheet.createRow(line + i + 1);
            // 填入数据
            Cell dataCell0 = dataRow.createCell(0);
            dataCell0.setCellValue(staffFeeObj.getString("userName"));
            dataCell0.setCellStyle(cellStyle);
            Cell dataCell1 = dataRow.createCell(1);
            dataCell1.setCellValue(staffFeeObj.getString("userId"));
            dataCell1.setCellStyle(cellStyle);
            Cell dataCell2 = dataRow.createCell(2);
            dataCell2.setCellValue(staffFeeObj.getString("receivableAmount"));
            dataCell2.setCellStyle(cellStyle);
            Cell dataCell3 = dataRow.createCell(3);
            dataCell3.setCellValue(staffFeeObj.getString("receivedAmount"));
            dataCell3.setCellStyle(cellStyle);
            dataRow.setHeight(defaultRowHeight);

            totalReceivedAmount = totalReceivedAmount.add(new BigDecimal(staffFeeObj.getString("receivedAmount")));
        }

        Row totalAmountRow = sheet.createRow(line + staffFees.size() + 1);
        Cell totalAmountCell0 = totalAmountRow.createCell(0);
        totalAmountCell0.setCellValue("实收总金额");
        totalAmountCell0.setCellStyle(cellStyle);
        Cell totalAmountCell1 = totalAmountRow.createCell(1);
        totalAmountCell1.setCellValue(totalReceivedAmount.doubleValue());
        totalAmountCell1.setCellStyle(cellStyle);
        totalAmountRow.setHeight(defaultRowHeight);

        return workbook;
    }

    private JSONArray getStaffFees(IPageData pd, JSONObject paramIn) {
        Integer page = paramIn.getInteger("page");
        Integer pageSize = paramIn.getInteger("row");
        paramIn.put("page", (page - 1) * pageSize );
        paramIn.put("row", page * pageSize);
        // 远程地址
        String apiUrl = ServiceConstant.SERVICE_API_URL +  "/api/api.getStaffFee" + mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "", apiUrl, HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK){
            return null;
        }

        JSONObject staffFeesInfoResults = JSONObject.parseObject(responseEntity.getBody(), Feature.OrderedField);
        if (!staffFeesInfoResults.containsKey("staffFees")){
            return null;
        }
        return staffFeesInfoResults.getJSONArray("staffFees");
    }

}

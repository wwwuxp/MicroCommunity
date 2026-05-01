package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.attendance.PassRecordDto;
import com.java110.front.smo.attendanceManagement.IExportPassRecordSMO;
import com.java110.intf.community.IPassRecordInnerServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.passRecord.ApiPassRecordDataVo;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * @author hh
 * @date 2022/1/26
 */
@Service("exportPassRecordSMOImpl")
public class ExportPassRecordSMOImpl extends BaseComponentSMO implements IExportPassRecordSMO {

    @Autowired
    private IPassRecordInnerServiceSMO passRecordInnerServiceSMOImpl;

    @Override
    public ResponseEntity<Object> exportPassRecord(IPageData pd) {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        PassRecordDto passRecordDto = BeanConvertUtil.covertBean(paramIn, PassRecordDto.class);

        checkTime(passRecordDto);

        //创建工作薄对象
        Workbook workbook = new XSSFWorkbook();
        //创建工作表对象
        Sheet sheet = workbook.createSheet("通行记录");
        // 设置列宽
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 6000);

        Row row0 = sheet.createRow(0);
        row0.setHeight((short) 500);
        CellStyle titleStyle = getTitleStyle(workbook);
        createTitleCell(row0, titleStyle);
        // 根据页码数决定查多少次
        int count = passRecordInnerServiceSMOImpl.queryPassRecordsCount(passRecordDto);
        if (count <= 0){
            return new ResponseEntity<>("没有数据可以导出", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 总页数
        int row = 500;
        int totalPage = (int) Math.ceil((double) count / (double) row);
        int num = 0;
        for (int i = 1; i <= totalPage; i++) {
            passRecordDto.setPage(i);
            passRecordDto.setRow(row);
            List<ApiPassRecordDataVo> apiPassRecordDataVos = passRecordInnerServiceSMOImpl.queryPassRecords(passRecordDto);

            for (ApiPassRecordDataVo apiPassRecordDataVo : apiPassRecordDataVos) {
                num++;
                Row dataRow = sheet.createRow(num);
                dataRow.setHeight((short) 400);
                Cell cell00 = dataRow.createCell(0);
                cell00.setCellValue(apiPassRecordDataVo.getName());
                Cell cell11 = dataRow.createCell(1);
                cell11.setCellValue(apiPassRecordDataVo.getDepartment());
                Cell cell22 = dataRow.createCell(2);
                cell22.setCellValue(apiPassRecordDataVo.getDeviceName());
                Cell cell33 = dataRow.createCell(3);
                cell33.setCellValue(apiPassRecordDataVo.getPassTime());
            }
        }

        String fileName = new String("通行记录数据".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        MultiValueMap<String, String> headers = generateHeaders(fileName);

        ByteArrayOutputStream os = null;
        byte[] context;
        try {
            os = new ByteArrayOutputStream();
            workbook.write(os);
            context = os.toByteArray();
        } catch (IOException e) {
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

    /**
     * 生成headers
     * @param fileName 文件名字
     * @return MultiValueMap
     */
    private MultiValueMap<String, String> generateHeaders(String fileName) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        headers.add("Pargam", "no-cache");
        headers.add("Cache-Control", "no-cache");
        headers.add("Accept-Ranges", "bytes");
        return headers;
    }

    /**
     * 校验时间
     * @param passRecordDto 请求参数
     */
    private void checkTime(PassRecordDto passRecordDto) {
        String smallTimeStr = passRecordDto.getSmallTime();
        String bigTimeStr = passRecordDto.getBigTime();
        String smallDateStr = passRecordDto.getSmallDate();
        String bigDateStr = passRecordDto.getBigDate();

        // 前端传递的日期时间必须符合标准格式，否则报错
        LocalTime smallTime = null;
        LocalTime bigTime = null;
        LocalDate smallDate = null;
        LocalDate bigDate = null;
        if (StringUtils.isNotBlank(smallTimeStr)){
            smallTime = LocalTime.parse(smallTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
        }
        if (StringUtils.isNotBlank(bigTimeStr)){
            bigTime = LocalTime.parse(bigTimeStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_M));
        }
        if (StringUtils.isNotBlank(smallDateStr)){
            smallDate = LocalDate.parse(smallDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        }
        if (StringUtils.isNotBlank(bigDateStr)){
            bigDate = LocalDate.parse(bigDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        }

        if (Objects.nonNull(smallTime) && Objects.nonNull(bigTime) && smallTime.isAfter(bigTime)){
            throw new IllegalArgumentException("开始时间不能大于结束时间");
        }
        if (Objects.nonNull(smallDate) && Objects.nonNull(bigDate) && smallDate.isAfter(bigDate)){
            throw new IllegalArgumentException("开始日期不能大于结束日期");
        }

        if (Objects.isNull(smallTime)){
            passRecordDto.setSmallTime(null);
        }
        if (Objects.isNull(bigTime)){
            passRecordDto.setBigTime(null);
        }
        if (Objects.isNull(smallDate)){
            passRecordDto.setSmallDate(null);
        }
        if (Objects.isNull(bigDate)){
            passRecordDto.setBigDate(null);
        }
    }

    /**
     * 创建标题的单元格
     * @param row0 标题行
     * @param titleStyle 标题样式
     */
    private void createTitleCell(Row row0, CellStyle titleStyle) {
        Cell cell0 = row0.createCell(0);
        cell0.setCellValue("姓名");
        cell0.setCellStyle(titleStyle);
        Cell cell1 = row0.createCell(1);
        cell1.setCellValue("科室");
        cell1.setCellStyle(titleStyle);
        Cell cell2 = row0.createCell(2);
        cell2.setCellValue("设备名");
        cell2.setCellStyle(titleStyle);
        Cell cell3 = row0.createCell(3);
        cell3.setCellValue("通行时间");
        cell3.setCellStyle(titleStyle);
    }


    /**
     * 生成头部标题样式
     * @param workbook workbook
     */
    private CellStyle getTitleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        // 粗体
        Font font = workbook.createFont();
        font.setBold(true);
        titleStyle.setFont(font);
        // 水平居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

}

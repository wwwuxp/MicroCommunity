package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.attendance.AttendanceExceptionDto;
import com.java110.front.smo.attendanceManagement.IExportAttendanceExceptionSMO;
import com.java110.intf.community.IAttendanceExceptionInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendanceException.ApiAttendanceExceptionDataVo;
import com.java110.vo.api.attendanceException.AttendanceExceptionDetail;
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
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 导出考勤异常数据
 * @author hh
 * @date 2021/11/26
 */
@Service("exportAttendanceExceptionSMOImpl")
public class ExportAttendanceExceptionSMOImpl extends BaseComponentSMO implements IExportAttendanceExceptionSMO {

    @Autowired
    private IAttendanceExceptionInnerServiceSMO attendanceExceptionInnerServiceSMOImpl;

    @Override
    public ResponseEntity<Object> exportAttendanceException(IPageData pd) throws UnsupportedEncodingException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "attendanceMonth", "请选择时间后进行导出");

        String attendanceMonth = paramIn.getString("attendanceMonth").trim();
        String attendanceEndMonth = paramIn.getString("attendanceEndMonth").trim();
        // 得到标准的日期格式,否则使用LocalDate解析出现错误
        String[] attendanceMonthSplit = attendanceMonth.split("-");
        String attendanceMonthValue = attendanceMonthSplit[1];
        if (attendanceMonthValue.length() == 1){
            attendanceMonthValue = "0" + attendanceMonthValue;
        }
        attendanceMonth = attendanceMonthSplit[0] + "-" + attendanceMonthValue + "-" + attendanceMonthSplit[2];

        Set<String> attendanceMonthSet = new HashSet<>();
        attendanceMonthSet.add(attendanceMonth);
        // 解析时间
        LocalDate attendanceMonthDate = LocalDate.parse(attendanceMonth, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        LocalDate attendanceEndMonthDate = null;
        if (StringUtils.isNotBlank(attendanceEndMonth)) {
            String[] attendanceEndMonthSplit = attendanceEndMonth.split("-");
            String attendanceEndMonthValue = attendanceEndMonthSplit[1];
            if (attendanceEndMonthValue.length() == 1) {
                attendanceEndMonthValue = "0" + attendanceEndMonthValue;
            }
            attendanceEndMonth = attendanceEndMonthSplit[0] + "-" + attendanceEndMonthValue + "-01";

            attendanceEndMonthDate = LocalDate.parse(attendanceEndMonth, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
            if (attendanceEndMonthDate.isBefore(attendanceMonthDate)){
                throw new IllegalArgumentException("结束时间不能在开始时间之前");
            }

            LocalDate tempDate = attendanceMonthDate;
            while (tempDate.isBefore(attendanceEndMonthDate)){
                tempDate = tempDate.plusMonths(1);
                attendanceMonthSet.add(tempDate.toString());
            }
        }
        AttendanceExceptionDto attendanceExceptionDto = new AttendanceExceptionDto();
        attendanceExceptionDto.setName(paramIn.getString("name"));
        attendanceExceptionDto.setDepartment(paramIn.getString("department"));
        attendanceExceptionDto.setPosition(paramIn.getString("position"));
        attendanceExceptionDto.setRange(attendanceMonthSet);

        String fileName;
        if (attendanceMonthDate.getYear() == attendanceEndMonthDate.getYear() && attendanceMonthDate.getMonthValue() == attendanceEndMonthDate.getMonthValue()){
            fileName = attendanceMonthDate.getYear() + "年" + attendanceMonthDate.getMonthValue() + "月考勤异常数据";
        }else {
            fileName = attendanceMonthDate.getYear() + "年" + attendanceMonthDate.getMonthValue() + "月到" +
                    attendanceEndMonthDate.getYear() + "年" + attendanceEndMonthDate.getMonthValue() + "月考勤异常数据";
        }
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        ByteArrayOutputStream os = null;
        byte[] context = new byte[0];
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
            Sheet sheet = workbook.createSheet("考勤异常");
            // 设置列宽
            sheet.setColumnWidth(0, 3000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 3000);
            sheet.setColumnWidth(4, 4000);
            sheet.setColumnWidth(5, 4000);
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
            cell2.setCellValue("考勤日期");
            cell2.setCellStyle(titleStyle);
            Cell cell3 = row0.createCell(3);
            cell3.setCellValue("日期类型");
            cell3.setCellStyle(titleStyle);
            Cell cell4 = row0.createCell(4);
            cell4.setCellValue("实际上班时间");
            cell4.setCellStyle(titleStyle);
            Cell cell5 = row0.createCell(5);
            cell5.setCellValue("实际下班时间");
            cell5.setCellStyle(titleStyle);
            Cell cell6 = row0.createCell(6);
            cell6.setCellValue("类型");
            cell6.setCellStyle(titleStyle);

            // 根据数据库中页码数决定写多少次
            int count = attendanceExceptionInnerServiceSMOImpl.queryAttendanceExceptionsCount(attendanceExceptionDto);
            if (count <= 0){
                return new ResponseEntity<>("没有数据可以导出", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // 总页数
            int row = 500;
            int totalPage = (int) Math.ceil((double) count / (double) row);
            int num = 0;
            for (int i = 1; i <= totalPage; i++) {
                attendanceExceptionDto.setPage(i);
                attendanceExceptionDto.setRow(row);
                List<ApiAttendanceExceptionDataVo> apiAttendanceExceptionDataVos = attendanceExceptionInnerServiceSMOImpl.queryAttendanceExceptions(attendanceExceptionDto);

                for (ApiAttendanceExceptionDataVo attendanceExceptionDataVo : apiAttendanceExceptionDataVos) {
                    List<AttendanceExceptionDetail> exceptionData = attendanceExceptionDataVo.getExceptionData();
                    for (AttendanceExceptionDetail exceptionDetail : exceptionData) {
                        num++;
                        Row rowi = sheet.createRow(num);
                        rowi.setHeight((short) 400);
                        Cell cell00 = rowi.createCell(0);
                        cell00.setCellValue(attendanceExceptionDataVo.getName());
                        Cell cell11 = rowi.createCell(1);
                        cell11.setCellValue(attendanceExceptionDataVo.getDepartment());
                        Cell cell22 = rowi.createCell(2);
                        cell22.setCellValue(exceptionDetail.getAttendanceDate());
                        Cell cell33 = rowi.createCell(3);
                        switch (exceptionDetail.getIsWorkingDay()){
                            case 1: cell33.setCellValue("工作日");break;
                            case 0: cell33.setCellValue("工作日");break;
                            default:
                        }
                        Cell cell44 = rowi.createCell(4);
                        cell44.setCellValue(exceptionDetail.getActualStartWorkTime());
                        Cell cell55 = rowi.createCell(5);
                        cell55.setCellValue(exceptionDetail.getActualEndWorkTime());
                        Cell cell66 = rowi.createCell(6);
                        switch (exceptionDetail.getType()){
                            case 1:
                                cell66.setCellValue("正常");break;
                            case 2:
                                cell66.setCellValue("迟到");break;
                            case 3:
                                cell66.setCellValue("早退");break;
                            case 4:
                                cell66.setCellValue("旷工");break;
                            default:
                        }
                    }
                }
            }
            os = new ByteArrayOutputStream();
            workbook.write(os);
            context = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            new ResponseEntity<>("导出失败", HttpStatus.INTERNAL_SERVER_ERROR);
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

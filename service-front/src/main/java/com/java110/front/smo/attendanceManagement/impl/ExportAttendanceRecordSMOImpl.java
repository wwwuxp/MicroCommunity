package com.java110.front.smo.attendanceManagement.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.PageDto;
import com.java110.dto.attendance.AttendanceRecordDto;
import com.java110.enums.AttendanceType;
import com.java110.front.mapper.AttendanceRecordMapper;
import com.java110.front.smo.attendanceManagement.IExportAttendanceRecordSMO;
import com.java110.intf.community.IAttendanceRecordInnerServiceSMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.vo.api.attendanceRecord.ApiAttendanceRecordDataVo;
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
import java.util.List;

/**
 * 考勤记录导出
 * @author hh
 * @date 2021/11/24
 */
@Service("exportAttendanceRecordSMOImpl")
public class ExportAttendanceRecordSMOImpl extends BaseComponentSMO implements IExportAttendanceRecordSMO {

    @Autowired
    private IAttendanceRecordInnerServiceSMO attendanceRecordInnerServiceSMOImpl;

    @Autowired
    private AttendanceRecordMapper attendanceRecordMapper;

    @Override
    public ResponseEntity<Object> exportAttendanceRecord(IPageData pd) throws UnsupportedEncodingException {
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "lessAttendanceDate", "请选择时间后进行导出");
        Assert.hasKeyAndValue(paramIn, "moreAttendanceDate", "请选择时间后进行导出");

        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setName(paramIn.getString("name"));
        attendanceRecordDto.setDepartment(paramIn.getString("department"));
        attendanceRecordDto.setPosition(paramIn.getString("position"));
        attendanceRecordDto.setWorkday(paramIn.getInteger("workday"));
        String lessAttendanceDateStr = paramIn.getString("lessAttendanceDate").trim();
        String moreAttendanceDateStr = paramIn.getString("moreAttendanceDate").trim();

        // 得到标准的日期格式,否则使用LocalDate解析出现错误
        String[] lessAttendanceDateArray = lessAttendanceDateStr.split("-");
        String lessAttendanceMonthValue = lessAttendanceDateArray[1];
        if (lessAttendanceMonthValue.length() == 1){
            lessAttendanceMonthValue = "0" + lessAttendanceMonthValue;
        }
        lessAttendanceDateStr = lessAttendanceDateArray[0] + "-" + lessAttendanceMonthValue + "-" + lessAttendanceDateArray[2];

        String[] moreAttendanceDateArray = moreAttendanceDateStr.split("-");
        String moreAttendanceMonthValue = moreAttendanceDateArray[1];
        if (moreAttendanceMonthValue.length() == 1){
            moreAttendanceMonthValue = "0" + moreAttendanceMonthValue;
        }
        moreAttendanceDateStr = moreAttendanceDateArray[0] + "-" + moreAttendanceMonthValue + "-" + moreAttendanceDateArray[2];

        attendanceRecordDto.setLessAttendanceDate(lessAttendanceDateStr);
        attendanceRecordDto.setMoreAttendanceDate(moreAttendanceDateStr);
        LocalDate lessDate = LocalDate.parse(lessAttendanceDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        LocalDate moreDate = LocalDate.parse(moreAttendanceDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        if (moreDate.isBefore(lessDate)){
            throw new IllegalArgumentException("结束时间不能在开始时间之前");
        }

        String fileName = lessDate.getYear() + "年" + lessDate.getMonthValue() + "月" + lessDate.getDayOfMonth() + "日到" +
                    moreDate.getYear() + "年" + moreDate.getMonthValue() + "月" + moreDate.getDayOfMonth() + "日考勤记录数据";

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
            Sheet sheet = workbook.createSheet("考勤记录");
            // 设置列宽
            sheet.setColumnWidth(0, 3000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 5000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 3000);
            sheet.setColumnWidth(5, 4000);
            sheet.setColumnWidth(6, 4000);
            sheet.setColumnWidth(7, 3000);
            sheet.setColumnWidth(8, 15000);

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
            Cell cell8 = row0.createCell(2);
            cell8.setCellValue("职务");
            cell8.setCellStyle(titleStyle);
            Cell cell2 = row0.createCell(3);
            cell2.setCellValue("考勤日期");
            cell2.setCellStyle(titleStyle);
            Cell cell3 = row0.createCell(4);
            cell3.setCellValue("日期类型");
            cell3.setCellStyle(titleStyle);
            Cell cell4 = row0.createCell(5);
            cell4.setCellValue("实际上班时间");
            cell4.setCellStyle(titleStyle);
            Cell cell5 = row0.createCell(6);
            cell5.setCellValue("实际下班时间");
            cell5.setCellStyle(titleStyle);
            Cell cell6 = row0.createCell(7);
            cell6.setCellValue("类型");
            cell6.setCellStyle(titleStyle);
            Cell cell7 = row0.createCell(8);
            cell7.setCellValue("备注");
            cell7.setCellStyle(titleStyle);

            // 根据数据库中页码数决定写多少次
//            int count = attendanceRecordInnerServiceSMOImpl.queryAttendanceRecordsCount(attendanceRecordDto);
//            int count = attendanceRecordMapper.queryAttendanceRecordsCount(attendanceRecordDto);
//            if (count <= 0){
//                return new ResponseEntity<>("没有数据可以导出", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
            // 总页数
//            int row = 500;
//            int totalPage = (int) Math.ceil((double) count / (double) row);
            int num = 0;
//            for (int i = 1; i <= totalPage; i++) {
//                attendanceRecordDto.setPage(i);
//                attendanceRecordDto.setRow(row);
//                List<ApiAttendanceRecordDataVo> apiAttendanceRecordDataVos = attendanceRecordInnerServiceSMOImpl.queryAttendanceRecords(attendanceRecordDto);

//                if (attendanceRecordDto.getPage() != PageDto.DEFAULT_PAGE) {
//                    attendanceRecordDto.setPage((attendanceRecordDto.getPage() - 1) * attendanceRecordDto.getRow());
//                }
            attendanceRecordDto.setPage(-1);
                List<ApiAttendanceRecordDataVo> apiAttendanceRecordDataVos = attendanceRecordMapper.queryAttendanceRecords(attendanceRecordDto);
                for (ApiAttendanceRecordDataVo apiAttendanceRecordDataVo : apiAttendanceRecordDataVos) {
                    num++;
                    Row dataRow = sheet.createRow(num);
                    dataRow.setHeight((short) 400);
                    Cell cell00 = dataRow.createCell(0);
                    cell00.setCellValue(apiAttendanceRecordDataVo.getName());
                    Cell cell11 = dataRow.createCell(1);
                    cell11.setCellValue(apiAttendanceRecordDataVo.getDepartment());
                    Cell cell88 = dataRow.createCell(2);
                    cell88.setCellValue(apiAttendanceRecordDataVo.getPosition());
                    Cell cell22 = dataRow.createCell(3);
                    cell22.setCellValue(apiAttendanceRecordDataVo.getAttendanceDate());
                    Cell cell33 = dataRow.createCell(4);
                    switch (apiAttendanceRecordDataVo.getIsWorkingDay()){
                        case 1: cell33.setCellValue("工作日");break;
                        case 0: cell33.setCellValue("非工作日");break;
                        default:
                    }
                    Cell cell44 = dataRow.createCell(5);
                    cell44.setCellValue(apiAttendanceRecordDataVo.getActualStartWorkTime());
                    Cell cell55 = dataRow.createCell(6);
                    cell55.setCellValue(apiAttendanceRecordDataVo.getActualEndWorkTime());
                    Cell cell66 = dataRow.createCell(7);
                    cell66.setCellValue(AttendanceType.getNameByCode(apiAttendanceRecordDataVo.getType()));
                    Cell cell77 = dataRow.createCell(8);
                    cell77.setCellValue(apiAttendanceRecordDataVo.getRemark());
//                    switch (apiAttendanceRecordDataVo.getType()){
//                        case 1:
//                            cell66.setCellValue("正常");break;
//                        case 2:
//                            cell66.setCellValue("迟到");break;
//                        case 3:
//                            cell66.setCellValue("早退");break;
//                        case 4:
//                            cell66.setCellValue("旷工");break;
//                        default:
//                    }
//                }
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

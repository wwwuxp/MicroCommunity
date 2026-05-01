package com.java110.front.smo.pubCarReimburse.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.properties.code.Java110Properties;
import com.java110.core.client.OssUploadTemplate;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.dto.file.FileDto;
import com.java110.dto.file.FileRelDto;
import com.java110.dto.pubCarReimburse.PubCarReimburseDto;
import com.java110.front.smo.pubCarReimburse.IExportPubCarReimburseSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.pubcar.IPubCarReimburseInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.OSSUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.pubCarReimburse.ApiPubCarReimburseDataVo;
import com.java110.vo.api.pubCarReimburse.ReimburseDetailInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用车报销 - 导出
 * @author hh
 * @date 2021/10/27
 */
@Service("exportPubCarReimburseSMOImpl")
public class ExportPubCarReimburseSMOImpl extends BaseComponentSMO implements IExportPubCarReimburseSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Java110Properties java110Properties;

    @Autowired
    private OssUploadTemplate ossUploadTemplate;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IPubCarReimburseInnerServiceSMO pubCarReimburseInnerServiceSMOImpl;

    @Override
    public ResponseEntity<Object> exportPubCarReimburse(IPageData pd) {
        this.validateStoreStaffCommunityRelationship(pd, restTemplate);
        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        Assert.hasKeyAndValue(paramIn, "communityId", "communityId参数不能为空");
        Assert.hasKeyAndValue(paramIn, "lessCarUseDate", "lessCarUseDate参数不能为空");
        Assert.hasKeyAndValue(paramIn, "moreCarUseDate", "moreCarUseDate参数不能为空");

        String lessCarUseDateStr = paramIn.getString("lessCarUseDate").trim();
        String moreCarUseDateStr = paramIn.getString("moreCarUseDate").trim();

        // 得到标准的日期格式,否则使用LocalDate解析出现错误
        String[] lessCarUseDateArray = lessCarUseDateStr.split("-");
        String lessCarUseMonthValue = lessCarUseDateArray[1];
        if (lessCarUseMonthValue.length() == 1){
            lessCarUseMonthValue = "0" + lessCarUseMonthValue;
        }
        lessCarUseDateStr = lessCarUseDateArray[0] + "-" + lessCarUseMonthValue + "-" + lessCarUseDateArray[2];

        String[] moreCarUseDateArray = moreCarUseDateStr.split("-");
        String moreCarUseMonthValue = moreCarUseDateArray[1];
        if (moreCarUseMonthValue.length() == 1){
            moreCarUseMonthValue = "0" + moreCarUseMonthValue;
        }
        moreCarUseDateStr = moreCarUseDateArray[0] + "-" + moreCarUseMonthValue + "-" + moreCarUseDateArray[2];

        LocalDate lessCarUseDate = LocalDate.parse(lessCarUseDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        LocalDate moreCarUseDate = LocalDate.parse(moreCarUseDateStr, DateTimeFormatter.ofPattern(DateUtil.DATE_FORMATE_STRING_B));
        if (moreCarUseDate.isBefore(lessCarUseDate)){
            throw new IllegalArgumentException("结束时间不能在开始时间之前");
        }
        String title = lessCarUseDate.getYear() + "年" + lessCarUseDate.getMonthValue() + "月用车报销";

        // 获取报销数据
        List<ApiPubCarReimburseDataVo> pubCarReimburseDatas = getPubCarReimburseDatas(paramIn);
        if (CollectionUtils.isEmpty(pubCarReimburseDatas)){
            throw new SMOException(ResponseConstant.RESULT_CODE_INNER_ERROR, "没有报销数据");
        }

        // 创建word文件
        XWPFDocument document = new XWPFDocument();
        // 创建标题
        createTitle(title, document);

        // 创建表格（展示所有人的整体情况）并填充数据
        createTableAndSetData(pubCarReimburseDatas, document);

        // 分页
        dividePage(document);

        // 批量获取文件保存名字
        List<String> fileIds = new ArrayList<>();
        for (ApiPubCarReimburseDataVo pubCarReimburseData : pubCarReimburseDatas) {
            List<ReimburseDetailInfo> reimburses = pubCarReimburseData.getReimburses();
            for (ReimburseDetailInfo reimburse : reimburses) {
                String[] invoices = reimburse.getInvoices();
                if (ArrayUtils.isNotEmpty(invoices)){
                    fileIds.addAll(Arrays.asList(invoices));
                }
            }
        }
        List<FileRelDto> fileRelDtos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fileIds)){
            fileRelDtos = fileRelInnerServiceSMOImpl.queryFileSaveNameBatch(fileIds);
        }

        for (ApiPubCarReimburseDataVo reimburseData : pubCarReimburseDatas) {
            // 创建表格
            XWPFTable personalTable = document.createTable(2, 4);
            // 每列的宽度自适应
            CTTblWidth ctTblWidth = personalTable.getCTTbl().addNewTblPr().addNewTblW();
            ctTblWidth.setType(STTblWidth.DXA);
            ctTblWidth.setW(BigInteger.valueOf(8300));

            setTitleData(personalTable);
            List<XWPFTableRow> rows = personalTable.getRows();
            XWPFTableRow tableRow = rows.get(1);
            setTableData(reimburseData, tableRow);

            // 换行
            XWPFParagraph newLineParagraph = document.createParagraph();
            XWPFRun run = newLineParagraph.createRun();
            run.addCarriageReturn();
            // run.addCarriageReturn();

            // 展示每项报销的详情
            List<ReimburseDetailInfo> reimburseInfos = reimburseData.getReimburses();
            for (ReimburseDetailInfo reimburseInfo : reimburseInfos) {
                run.setText("费用类别：" + convertFeeType(reimburseInfo.getFeeType()));
                run.addCarriageReturn();
                run.setText("报销金额：" + reimburseInfo.getReimburseAmount());
                run.addCarriageReturn();
                run.setText("用车日期：" + reimburseInfo.getCarUseDate());
                run.addCarriageReturn();
                run.setText("出发地：" + reimburseInfo.getStart());
                run.addCarriageReturn();
                run.setText("目的地：" + reimburseInfo.getDestination());
                run.addCarriageReturn();

                // 该invoices中保存的是上传的电子发票文件id
                String[] invoices = reimburseInfo.getInvoices();

                // 展示该项报销的发票
                if (ArrayUtils.isNotEmpty(invoices)) {
                    for (String invoice : invoices) {
                        List<FileRelDto> filterFileRelDtos = fileRelDtos.stream().filter(t -> invoice.equals(t.getFileRealName())).collect(Collectors.toList());
                        FileDto fileDto = new FileDto();
                        if (fileRelDtos.size() < 1) {
                            fileDto.setFileSaveName(invoice);
                        } else {
                            fileDto.setFileSaveName(filterFileRelDtos.get(0).getFileSaveName());
                        }

                        String fileName = fileDto.getFileSaveName();
                        String ftpPath = java110Properties.getFtpPath();
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if (fileName.contains("/")) {
                            ftpPath += fileName.substring(0, fileName.lastIndexOf("/") + 1);
                            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                        }
                        byte[] bytes = null;
                        String ossSwitch = MappingCache.getValue(OSSUtil.DOMAIN, OSSUtil.OSS_SWITCH);
                        if (StringUtil.isEmpty(ossSwitch) || !OSSUtil.OSS_SWITCH_OSS.equals(ossSwitch)) {
                            // TODO
                            // context = ftpUploadTemplate.download(ftpPath, fileName, java110Properties.getFtpServer(),
                            //         java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                            //         java110Properties.getFtpUserPassword());
                        } else {
                            bytes = ossUploadTemplate.downFileByte(ftpPath, fileName, java110Properties.getFtpServer(),
                                    java110Properties.getFtpPort(), java110Properties.getFtpUserName(),
                                    java110Properties.getFtpUserPassword());
                        }
                        if (bytes != null) {
                            try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
                                run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, fileName, Units.toEMU(200), Units.toEMU(200));
                            } catch (IOException | InvalidFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        // 回车
                        run.addCarriageReturn();
                    }
                }

                run.addCarriageReturn();
            }

            // 分页
            dividePage(document);
        }

        String fileName = lessCarUseDate.getYear() + "年" + lessCarUseDate.getMonthValue() + "月" + lessCarUseDate.getDayOfMonth() + "日到" +
                moreCarUseDate.getYear() + "年" + moreCarUseDate.getMonthValue() + "月" + moreCarUseDate.getDayOfMonth() + "日用车报销数据";

        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("content-type", "application/octet-stream;charset=UTF-8");
        headers.add("Content-Disposition", "attachment;filename=" + fileName + ".docx");
        headers.add("Pargam", "no-cache");
        headers.add("Cache-Control", "no-cache");
        headers.add("Accept-Ranges", "bytes");
        byte[] context;
        try {
            document.write(os);
            context = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("导出失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(context, headers, HttpStatus.OK);
    }

    /**
     * 费用类型转换
     * @param feeType 费用类别。1：停车费，2：加油费，3：高速费，4：其它费用
     * @return string
     */
    private String convertFeeType(Integer feeType) {
        if (Objects.isNull(feeType)){
            return "";
        }
        switch (feeType){
            case 1: return "停车费";
            case 2: return "加油费";
            case 3: return "高速费";
            case 4: return "其它费用";
            default: return "";
        }
    }

    /**
     * 填充表格数据
     * @param reimburseData 数据
     * @param tableRow tableRow
     */
    private void setTableData(ApiPubCarReimburseDataVo reimburseData, XWPFTableRow tableRow) {
        List<XWPFTableCell> tableCells = tableRow.getTableCells();
        for (int j = 0; j < tableCells.size(); j++) {
            XWPFParagraph cellParagraph = tableCells.get(j).getParagraphArray(0);
            cellParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun cellRun = cellParagraph.createRun();
            cellRun.setFontSize(10);
            switch (j){
                case 0:
                    cellRun.setText(reimburseData.getDepartment());
                    break;
                case 1:
                    cellRun.setText(reimburseData.getPosition());
                    break;
                case 2:
                    cellRun.setText(reimburseData.getName());
                    break;
                case 3:
                    cellRun.setText(reimburseData.getTotalAmount().toString() + "元");
                    break;
                default:
            }
        }
    }

    /**
     * 设置标题数据
     * @param table table
     */
    private void setTitleData(XWPFTable table) {
        // 获取标题行
        List<XWPFTableCell> titleRowCells = table.getRow(0).getTableCells();
        // 标题行填充数据
        XWPFTableCell titleCell1 = titleRowCells.get(0);
        titleCell1.setText("科室");
        XWPFTableCell titleCell2 = titleRowCells.get(1);
        titleCell2.setText("职务");
        XWPFTableCell titleCell3 = titleRowCells.get(2);
        titleCell3.setText("姓名");
        XWPFTableCell titleCell4 = titleRowCells.get(3);
        titleCell4.setText("金额");
        if (titleRowCells.size() == 5){
            XWPFTableCell titleCell5 = titleRowCells.get(4);
            titleCell5.setText("签名");
        }
    }

    /**
     * 分页
     * @param document document
     */
    private void dividePage(XWPFDocument document) {
        XWPFParagraph pageSplitParagraph = document.createParagraph();
        pageSplitParagraph.setPageBreak(true);
    }

    /**
     * 获取报销数据
     * @param paramIn 参数
     * @return list
     */
    private List<ApiPubCarReimburseDataVo> getPubCarReimburseDatas(JSONObject paramIn) {
        PubCarReimburseDto pubCarReimburseDto = new PubCarReimburseDto();
        pubCarReimburseDto.setCommunityId(paramIn.getString("communityId"));
        pubCarReimburseDto.setLessCarUseDate(paramIn.getString("lessCarUseDate"));
        pubCarReimburseDto.setMoreCarUseDate(paramIn.getString("moreCarUseDate"));
        pubCarReimburseDto.setRow(10000);
        pubCarReimburseDto.setPage(1);
        return pubCarReimburseInnerServiceSMOImpl.listPubCarReimburses(pubCarReimburseDto);
    }

    /**
     * 创建标题
     * @param title 标题内容
     * @param document document
     */
    private void createTitle(String title, XWPFDocument document) {
        // 创建段落
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(title);
        titleRun.setFontSize(21);
        // 回车换行
        titleRun.addCarriageReturn();
    }

    /**
     * 创建表格并填充数据
     * @param pubCarReimburseDatas 数据
     * @param document document
     */
    private void createTableAndSetData(List<ApiPubCarReimburseDataVo> pubCarReimburseDatas, XWPFDocument document) {
        // 创建表格(加的1是标题)
        XWPFTable table = document.createTable(pubCarReimburseDatas.size()+1, 5);

        // 每列的宽度自适应
        CTTblWidth ctTblWidth = table.getCTTbl().addNewTblPr().addNewTblW();
        ctTblWidth.setType(STTblWidth.DXA);
        ctTblWidth.setW(BigInteger.valueOf(8300));

        setTitleData(table);

        List<XWPFTableRow> rows = table.getRows();
        for (int i = 1; i < rows.size(); i++) {
            ApiPubCarReimburseDataVo reimburseData = pubCarReimburseDatas.get(i - 1);
            XWPFTableRow tableRow = rows.get(i);
            setTableData(reimburseData, tableRow);
        }
    }

}

package com.java110.ext.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.holder.xlsx.XlsxReadSheetHolder;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 同步数据Excel处理工具类
 * 支持多Sheet导出和导入
 */
public class SyncDataExcel {

    /**
     * Excel Sheet名称
     */
    public static final String SHEET_ORG = "组织";
    public static final String SHEET_STAFF = "员工";
    public static final String SHEET_REL = "员工组织关系";

    /**
     * 导出数据到Excel（多Sheet）
     *
     * @param out     输出流
     * @param orgs    组织列表
     * @param staffs  员工列表
     * @param rels    关系列表
     */
    public static void exportMultiSheet(java.io.OutputStream out, List<OrgExcel> orgs, List<StaffExcel> staffs, List<OrgStaffRelExcel> rels) {
        ExcelWriter excelWriter = EasyExcel.write(out).build();

        // 写入组织Sheet
        WriteSheet orgSheet = EasyExcel.writerSheet(0, SHEET_ORG)
                .head(OrgExcel.class)
                .build();
        excelWriter.write(orgs != null ? orgs : new ArrayList<>(), orgSheet);

        // 写入员工Sheet
        WriteSheet staffSheet = EasyExcel.writerSheet(1, SHEET_STAFF)
                .head(StaffExcel.class)
                .build();
        excelWriter.write(staffs != null ? staffs : new ArrayList<>(), staffSheet);

        // 写入关系Sheet
        WriteSheet relSheet = EasyExcel.writerSheet(2, SHEET_REL)
                .head(OrgStaffRelExcel.class)
                .build();
        excelWriter.write(rels != null ? rels : new ArrayList<>(), relSheet);

        excelWriter.finish();
    }

    /**
     * 从Excel导入数据（使用ExcelReader读取所有Sheet）
     *
     * @param inputStream Excel文件输入流
     * @param listener    数据读取监听器
     */
    public static void importData(InputStream inputStream, SyncDataReadListener listener) throws Exception {
        SyncDataReadListenerWrapper wrapper = new SyncDataReadListenerWrapper(listener);

        // 使用EasyExcel.read创建ExcelReader
        ExcelReader excelReader = EasyExcel.read(inputStream).build();

        // 注册所有Sheet的读取监听
        // Sheet 0: 组织
        ReadSheet orgSheet = EasyExcel.readSheet(SHEET_ORG)
                .head(OrgExcel.class)
                .registerReadListener(new OrgDataListener(wrapper))
                .build();
        excelReader.read(orgSheet);

        // Sheet 1: 员工
        ReadSheet staffSheet = EasyExcel.readSheet(SHEET_STAFF)
                .head(StaffExcel.class)
                .registerReadListener(new StaffDataListener(wrapper))
                .build();
        excelReader.read(staffSheet);

        // Sheet 2: 关系
        ReadSheet relSheet = EasyExcel.readSheet(SHEET_REL)
                .head(OrgStaffRelExcel.class)
                .registerReadListener(new RelDataListener(wrapper))
                .build();
        excelReader.read(relSheet);

        excelReader.finish();

        // 通知读取完成
        wrapper.notifyFinish();
    }

    /**
     * 数据读取监听器接口
     */
    public interface SyncDataReadListener {
        void onOrgData(OrgExcel org);
        void onStaffData(StaffExcel staff);
        void onRelData(OrgStaffRelExcel rel);
        void onFinish(int orgCount, int staffCount, int relCount);
    }

    /**
     * 监听器包装类，用于统计数量
     */
    private static class SyncDataReadListenerWrapper {
        private final SyncDataReadListener listener;
        private int orgCount = 0;
        private int staffCount = 0;
        private int relCount = 0;

        public SyncDataReadListenerWrapper(SyncDataReadListener listener) {
            this.listener = listener;
        }

        public void onOrgData(OrgExcel org) {
            orgCount++;
            listener.onOrgData(org);
        }

        public void onStaffData(StaffExcel staff) {
            staffCount++;
            listener.onStaffData(staff);
        }

        public void onRelData(OrgStaffRelExcel rel) {
            relCount++;
            listener.onRelData(rel);
        }

        public void notifyFinish() {
            listener.onFinish(orgCount, staffCount, relCount);
        }

        public int getOrgCount() {
            return orgCount;
        }

        public int getStaffCount() {
            return staffCount;
        }

        public int getRelCount() {
            return relCount;
        }
    }

    /**
     * 组织数据读取监听器
     */
    private static class OrgDataListener implements ReadListener<OrgExcel> {
        private final SyncDataReadListenerWrapper wrapper;

        public OrgDataListener(SyncDataReadListenerWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void onException(Exception e, AnalysisContext analysisContext) throws Exception {

        }

        @Override
        public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {

        }

        @Override
        public void invoke(OrgExcel data, com.alibaba.excel.context.AnalysisContext context) {
            wrapper.onOrgData(data);
        }

        @Override
        public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {

        }

        @Override
        public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
            // 读取完成
        }

        @Override
        public boolean hasNext(AnalysisContext analysisContext) {
            return false;
        }
    }

    /**
     * 员工数据读取监听器
     */
    private static class StaffDataListener implements ReadListener<StaffExcel> {
        private final SyncDataReadListenerWrapper wrapper;

        public StaffDataListener(SyncDataReadListenerWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void onException(Exception e, AnalysisContext analysisContext) throws Exception {

        }

        @Override
        public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {

        }

        @Override
        public void invoke(StaffExcel data, com.alibaba.excel.context.AnalysisContext context) {
            wrapper.onStaffData(data);
        }

        @Override
        public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {

        }

        @Override
        public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
            // 读取完成
        }

        @Override
        public boolean hasNext(AnalysisContext analysisContext) {
            return false;
        }
    }

    /**
     * 关系数据读取监听器
     */
    private static class RelDataListener implements ReadListener<OrgStaffRelExcel> {
        private final SyncDataReadListenerWrapper wrapper;

        public RelDataListener(SyncDataReadListenerWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void onException(Exception e, AnalysisContext analysisContext) throws Exception {

        }

        @Override
        public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {

        }

        @Override
        public void invoke(OrgStaffRelExcel data, com.alibaba.excel.context.AnalysisContext context) {
            wrapper.onRelData(data);
        }

        @Override
        public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {

        }

        @Override
        public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
            // 读取完成
        }

        @Override
        public boolean hasNext(AnalysisContext analysisContext) {
            return false;
        }
    }
}

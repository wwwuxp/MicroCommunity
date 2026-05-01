package com.java110.ext.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.java110.ext.excel.OrgStaffRelExcel;

import java.util.ArrayList;
import java.util.List;

public class OrgStaffExportListener extends AnalysisEventListener<OrgStaffRelExcel> {

    private final List<OrgStaffRelExcel> dataList = new ArrayList<>();
    @Override
    public void invoke(OrgStaffRelExcel orgStaffRelExcel, AnalysisContext analysisContext) {
        dataList.add(orgStaffRelExcel);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
    public List<OrgStaffRelExcel> getDataList(){
        return dataList;
    }
}

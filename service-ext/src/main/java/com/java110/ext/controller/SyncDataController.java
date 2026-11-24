package com.java110.ext.controller;

import com.alibaba.excel.EasyExcel;
import com.java110.ext.excel.OrgExcel;
import com.java110.ext.excel.OrgStaffRelExcel;
import com.java110.ext.excel.StaffExcel;
import com.java110.ext.excel.SyncDataExcel;
import com.java110.ext.excel.listener.OrgStaffExportListener;
import com.java110.ext.pojo.OrgPo;
import com.java110.ext.pojo.OrgStaffRelPo;
import com.java110.ext.pojo.UserPo;
import com.java110.ext.service.ISyncDataService;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户体系数据同步接口
 * 统一管理用户体系数据的同步、导出和导入（Excel格式）
 */
@RestController
@RequestMapping("/api/sync")
public class SyncDataController {

    private static final Logger logger = LoggerFactory.getLogger(SyncDataController.class);

    @Autowired
    private ISyncDataService syncDataService;

    /**
     * 手动触发数据同步
     * 从外部系统同步数据到本地数据库
     */
    @PostMapping("/sync")
    public ResponseEntity syncData() {
        logger.info("【手动同步】开始同步用户体系数据");
        Map<String, Object> result = syncDataService.syncAllData();
        logger.info("【手动同步】同步完成，结果: {}", result);
        return ResultVo.createResponseObject(result);
    }

    /**
     * 导出用户体系数据（Excel格式）
     * 导出一个包含3个Sheet的Excel文件：组织、员工、员工组织关系
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws IOException {

        logger.info("【导出数据】开始导出用户体系数据");

        // 准备查询参数
        Map<String, Object> params = new HashMap<>();

        // 查询数据
        List<OrgStaffRelExcel> data = syncDataService.exportData(params);

        // 发送文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String filename = "用户体系数据_" + System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        EasyExcel.write(response.getOutputStream(), OrgStaffRelExcel.class).sheet("用户体系").doWrite(data);
    }

    /**
     * 导入用户体系数据（Excel格式）
     * 上传Excel文件，包含组织、员工、员工组织关系数据
     */
    @PostMapping("/import")
    public ResponseEntity importData(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        logger.info("【导入数据】开始导入用户体系数据，文件名={}", file.getOriginalFilename());

        syncDataService.importData(file);

        return ResultVo.success();
    }

    @GetMapping("/writeToAttendanceSys")
    public ResponseEntity writeToAttendanceSys(){
        syncDataService.writeToAttendanceSys();
        return ResultVo.success();
    }

    @GetMapping("/syncFromVisitorDb")
    public ResponseEntity syncFromVisitorDb(){
        syncDataService.syncFromVisitorDb();
        return ResultVo.success();
    }

//    /**
//     * 查询导出数据预览（统计数量）
//     */
//    @GetMapping("/preview")
//    public ResponseEntity previewData(
//            @RequestParam(value = "sourceSystem", required = false) String sourceSystem,
//            @RequestParam(value = "orgId", required = false) String orgId,
//            @RequestParam(value = "storeId", required = false) String storeId) {
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("sourceSystem", sourceSystem);
//        params.put("orgId", orgId);
//        params.put("storeId", storeId);
//        params.put("previewOnly", true);
//
//        Map<String, Object> data = syncDataService.exportData(params);
//        return ResultVo.createResponseObject(data);
//    }

    // ==================== 私有辅助方法 ====================


//    private List<StaffExcel> convertToStaffExcel(List<StaffPo> staffs) {
//        List<StaffExcel> excelList = new ArrayList<>();
//        if (staffs == null) {
//            return excelList;
//        }
//        for (UserPo staff : staffs) {
//            StaffExcel excel = new StaffExcel();
//            excel.setStaffId(staff.getStaffId());
//            excel.setStoreId(staff.getStoreId());
//            excel.setStaffName(staff.getStaffName());
//            excel.setStaffCode(staff.getStaffCode());
//            excel.setTel(staff.getTel());
//            excel.setIdCard(staff.getIdCard());
//            excel.setSex(staff.getSex());
//            excel.setEmail(staff.getEmail());
//            excel.setAddress(staff.getAddress());
//            excel.setJoinDate(staff.getJoinDate());
//            excel.setStatusCd(staff.getStatusCd());
//            excel.setSourceSystem(staff.getSourceSystem());
//            excelList.add(excel);
//        }
//        return excelList;
//    }





}

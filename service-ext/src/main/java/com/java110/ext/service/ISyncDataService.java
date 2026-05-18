package com.java110.ext.service;

import com.java110.ext.excel.OrgStaffRelExcel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 数据同步服务接口
 * 提供用户体系数据的导出、导入和同步功能
 */
public interface ISyncDataService {

    /**
     * 同步用户体系全部数据（组织+员工+关系）
     * 从外部系统同步数据到本地数据库
     *
     * @return 同步结果，包含：
     *         - success: 是否成功
     *         - orgCount: 同步组织数
     *         - staffCount: 同步员工数
     *         - relCount: 同步关系数
     *         - failCount: 失败数
     *         - errors: 错误列表
     */
    Map<String, Object> syncAllData();

    /**
     * 导出用户体系数据
     *
     * @param params 查询参数：
     *               - sourceSystem: 来源系统标识
     *               - orgId: 指定组织ID（可选）
     *               - storeId: 指定商户ID（可选）
     *               - previewOnly: 是否仅预览（可选，默认false）
     * @return 导出数据，包含：
     *         - orgs: 组织列表
     *         - staffs: 员工列表
     *         - rels: 员工组织关系列表
     *         - totalOrg/totalStaff/totalRel: 统计数量
     *         - exportTime: 导出时间
     *         - sourceSystem: 来源系统
     */
    List<OrgStaffRelExcel> exportData(Map<String, Object> params);

    /**
     * 导入用户体系数据
     *
     * @param file 待导入的数据，包含：
     * @return 导入结果，包含：
     *         - success: 是否成功
     *         - taskId: 任务ID
     *         - orgCount: 导入组织数
     *         - staffCount: 导入员工数
     *         - relCount: 导入关系数
     *         - errors: 错误列表
     */
    void importData(MultipartFile file) throws IOException;

    void writeToAttendanceSys();
}

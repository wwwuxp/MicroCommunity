package com.java110.ext.service;

import com.java110.ext.pojo.attendance.SysDepartPo;
import com.java110.ext.pojo.attendance.SysUserDepartPo;
import com.java110.ext.pojo.attendance.SysUserPo;
import com.java110.ext.vo.SysUserDepartVo;

import java.util.List;

public interface AttendanceSysService {
    SysUserPo getSysUserBySourceUserId(String sourceUserId);

    List<SysUserPo> getSysUserByRealname(String userName);

    void updateSysUserById(SysUserPo sysUserPo);

    List<SysUserDepartVo> getUserDepartByUserId(String id);

    SysDepartPo getDepartIdByName(String orgName);

    void insertUserDepart(SysUserDepartPo sysUserDepartPo);

    void deleteSysDepartById(String userDepartId);

    /**
     * 获取visitor_db_guoshui中所有活跃用户
     */
    List<SysUserPo> getAllActiveSysUsers();

    /**
     * 获取visitor_db_guoshui中所有未删除的部门
     */
    List<SysDepartPo> getAllActiveDeparts();
}

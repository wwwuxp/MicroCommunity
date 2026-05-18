package com.java110.ext.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.java110.ext.mapper.atttendance.SysDepartMapper;
import com.java110.ext.mapper.atttendance.SysUserDepartMapper;
import com.java110.ext.mapper.atttendance.SysUserMapper;
import com.java110.ext.pojo.attendance.SysDepartPo;
import com.java110.ext.pojo.attendance.SysUserDepartPo;
import com.java110.ext.pojo.attendance.SysUserPo;
import com.java110.ext.service.AttendanceSysService;
import com.java110.ext.vo.SysUserDepartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DS("ds2")
@Service
public class AttendanceSysServiceImpl implements AttendanceSysService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private SysDepartMapper departMaoper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SysUserPo getSysUserBySourceUserId(String sourceUserId) {
        LambdaQueryWrapper<SysUserPo> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(SysUserPo::getSourceUserId, sourceUserId);
        userQuery.eq(SysUserPo::getDelFlag, 0);
        return sysUserMapper.selectOne(userQuery);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SysUserPo> getSysUserByRealname(String userName) {
        LambdaQueryWrapper<SysUserPo> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(SysUserPo::getRealname, userName);
        userQuery.eq(SysUserPo::getDelFlag, 0);
        return sysUserMapper.selectList(userQuery);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateSysUserById(SysUserPo sysUserPo) {
        sysUserMapper.updateById(sysUserPo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SysUserDepartVo> getUserDepartByUserId(String userId) {
        return sysUserMapper.getUserDepartByUserId(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SysDepartPo getDepartIdByName(String orgName) {
        LambdaQueryWrapper<SysDepartPo> departQuery = new LambdaQueryWrapper<>();
        departQuery.eq(SysDepartPo::getDepartName, orgName);
        departQuery.eq(SysDepartPo::getDelFlag, "0");
        return departMaoper.selectOne(departQuery);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertUserDepart(SysUserDepartPo sysUserDepartPo) {
        sysUserDepartMapper.insert(sysUserDepartPo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteSysDepartById(String userDepartId) {
        sysUserDepartMapper.deleteById(userDepartId);
    }
}

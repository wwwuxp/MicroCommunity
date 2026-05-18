package com.java110.ext.mapper.atttendance;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java110.ext.pojo.attendance.SysDepartPo;
import com.java110.ext.pojo.attendance.SysUserDepartPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserDepartMapper extends BaseMapper<SysUserDepartPo> {
}

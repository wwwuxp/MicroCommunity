package com.java110.ext.mapper.atttendance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java110.ext.pojo.attendance.SysUserPo;
import com.java110.ext.vo.SysUserDepartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserPo> {
    List<SysUserDepartVo> getUserDepartByUserId(@Param("userId") String userId);
}

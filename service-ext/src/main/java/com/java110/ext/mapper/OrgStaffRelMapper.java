package com.java110.ext.mapper;

import com.java110.ext.excel.OrgStaffRelExcel;
import com.java110.ext.vo.OrgStaffVo;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java110.ext.pojo.OrgStaffRelPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工组织关系Mapper
 */
@Mapper
public interface OrgStaffRelMapper extends BaseMapper<OrgStaffRelPo> {

    List<OrgStaffVo> selectOrgUser(@Param("sourceOrgId") String sourceOrgId, @Param("userId") String userId);

    List<OrgStaffRelExcel> selectExportSourceData();
}

package com.java110.ext.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java110.ext.pojo.OrgPo;

/**
 * 组织Mapper
 */
@Mapper
public interface OrgMapper extends BaseMapper<OrgPo> {

    /**
     * 根据组织ID查询
     *
     * @param orgId 组织ID
     * @return 组织信息
     */
    OrgPo selectByOrgId(@Param("orgId") String orgId);
}

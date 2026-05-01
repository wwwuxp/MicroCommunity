package com.java110.ext.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java110.ext.pojo.DemoPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 示例Mapper
 */
@Mapper
public interface DemoMapper extends BaseMapper<DemoPo> {
}

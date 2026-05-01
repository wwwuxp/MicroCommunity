package com.java110.ext.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java110.ext.pojo.SyncTaskLogPo;

/**
 * 同步任务日志Mapper
 */
@Mapper
public interface SyncTaskLogMapper extends BaseMapper<SyncTaskLogPo> {
}

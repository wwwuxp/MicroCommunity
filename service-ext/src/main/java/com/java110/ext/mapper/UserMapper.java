package com.java110.ext.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java110.ext.pojo.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

    Integer hasSameInvitorId(@Param("invitorId") String invitorId);

    List<String> selectUserIdByNotInUserAndFlagNotNull(@Param("exsitsUserId") List<String> exsitsUserId);
}

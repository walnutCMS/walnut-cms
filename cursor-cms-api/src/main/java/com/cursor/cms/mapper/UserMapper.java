package com.cursor.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cursor.cms.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
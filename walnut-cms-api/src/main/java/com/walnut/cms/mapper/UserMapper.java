package com.walnut.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.walnut.cms.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
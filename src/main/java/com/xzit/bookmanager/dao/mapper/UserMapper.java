package com.xzit.bookmanager.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzit.bookmanager.entity.AuthUser;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper extends BaseMapper<AuthUser> {
    @Select("select * from t_user where username=#{username}")
    AuthUser getUserByUsername(@Param("username") String username);

    @Insert("insert into t_user(username,password,role) values (#{user.username},#{user.password},#{user.role})")
    Integer insertUser(@Param("user") AuthUser user);

    @Select("select count(*) from t_user")
    Integer countUser();
    @Update("update t_user set password = #{password} where username=#{username}")
    Integer updateUserPassword(AuthUser user);

}


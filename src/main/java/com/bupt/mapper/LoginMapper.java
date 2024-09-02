package com.bupt.mapper;

import com.bupt.pojo.Login;
import com.bupt.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface LoginMapper {
    @Select("select * from test_db.user_info where user_name = #{userName} and password =#{password}")
    public Integer loginCheck(Login user);
    @Select("select user_name,role from test_db.user_info where user_name = #{username} and password =#{password}")
    public UserInfo tokenCheck(Map<String,Object> user);
}

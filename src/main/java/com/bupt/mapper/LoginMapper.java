package com.bupt.mapper;

import com.bupt.pojo.Login;
import com.bupt.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface LoginMapper {
    @Select("select UserName,Password,RoleId from test_db.Users where UserName = #{userName}")
    public UserInfo loginCheck(Login user);
    @Select("select UserName,RoleId,UserId,NickName from test_db.Users where UserName = #{username}")
    public UserInfo tokenCheck(Map<String,Object> user);
}

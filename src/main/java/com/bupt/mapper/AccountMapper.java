package com.bupt.mapper;

import com.bupt.pojo.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {
    public Integer searchByName(Account myAccount);
    public int createAccount(Account myAccount);

    public String searchPassWordById(Integer userId);
    public int changePassword(@Param("password") String password,@Param("userId") int userId);
}

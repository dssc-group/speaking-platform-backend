package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserInfo {
    private String userName;
    private String nickName;
    private String password;
    private Integer roleId;
    private Integer userId;
}

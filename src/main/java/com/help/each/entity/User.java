package com.help.each.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel implements Serializable {
    private String uuid;
    private String username;
    private String password;
    private String role;
    private String nickname;
    private String sex;
    private String avatar;
    private String email;
    private String phone;
    private Integer age;
    private Float points;
}

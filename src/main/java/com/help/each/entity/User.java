package com.help.each.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseModel {
    private String uuid;
    private String username;
    private String password;
    private String role;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private int age;
}

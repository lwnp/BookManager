package com.xzit.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class AuthUser {
    @TableId(value = "username",type = IdType.NONE)
    String username;
    @TableField(value = "password")
    String password;
    @TableField(value = "role")
    String role;

}

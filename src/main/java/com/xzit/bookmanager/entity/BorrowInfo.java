package com.xzit.bookmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_borrowInfo")
public class BorrowInfo {
    @TableId(value = "username",type = IdType.NONE)
    String username;
    Integer booknum;
}
